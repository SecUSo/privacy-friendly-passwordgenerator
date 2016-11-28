package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGeneratorTask;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by yonjuni on 14.11.16.
 */

public class UpdatePasswordDialog extends DialogFragment {

    Activity activity;
    View rootView;

    MetaDataSQLiteHelper database;
    int position;
    MetaData metaData;
    MetaData oldMetaData;

    boolean bindToDevice_enabled;
    String hashAlgorithm;
    int number_iterations;

    ProgressBar spinnerOld;
    ProgressBar spinnerNew;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rootView = inflater.inflate(R.layout.dialog_update_passwords, null);


        spinnerOld = (ProgressBar) rootView.findViewById(R.id.oldProgressBar);
        spinnerOld.setVisibility(View.GONE);

        spinnerNew = (ProgressBar) rootView.findViewById(R.id.newProgressBar);
        spinnerNew.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        position = bundle.getInt("position");
        bindToDevice_enabled = bundle.getBoolean("bindToDevice_enabled");
        Log.d("UPDATER BIND", Boolean.toString(bindToDevice_enabled));
        hashAlgorithm = bundle.getString("hash_algorithm");
        Log.d("UPDATER HASH", hashAlgorithm);
        setOldMetaData(bundle);
        number_iterations = bundle.getInt("number_iterations");

        this.database = new MetaDataSQLiteHelper(getActivity());

        builder.setView(rootView);

        builder.setIcon(R.mipmap.ic_drawer);

        builder.setTitle(getActivity().getString(R.string.passwords_heading));

        Button displayButton = (Button) rootView.findViewById(R.id.displayButton);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);


                    displayPasswords();
            }
        });

        ImageButton copyOldButton = (ImageButton) rootView.findViewById(R.id.copyOldButton);
        ImageButton copyNewButton = (ImageButton) rootView.findViewById(R.id.copyNewButton);

        copyOldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView oldPassword = (TextView) rootView.findViewById(R.id.textViewOldPassword);
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", oldPassword.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, activity.getString(R.string.copy_clipboar_old), Toast.LENGTH_SHORT).show();
            }
        });

        copyNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView newPassword = (TextView) rootView.findViewById(R.id.textViewNewPassword);
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", newPassword.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, activity.getString(R.string.copy_clipboar_new), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton(getActivity().getString(R.string.done), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickDone();
            }
        });

        return builder.create();
    }

    public void onClickDone() {
        activity.recreate();
        this.dismiss();
    }

    public void displayPasswords() {
        EditText editTextUpdateMasterpassword = (EditText) rootView.findViewById(R.id.editTextUpdateMasterpassword);

        if (editTextUpdateMasterpassword.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(activity.getBaseContext(), getString(R.string.enter_masterpassword), Toast.LENGTH_SHORT);
            toast.show();
        } else {

            spinnerOld.setVisibility(View.VISIBLE);
            spinnerNew.setVisibility(View.VISIBLE);

            final TextView textViewOldPassword = (TextView) rootView.findViewById(R.id.textViewOldPassword);
            final TextView textViewNewPassword = (TextView) rootView.findViewById(R.id.textViewNewPassword);

            String masterpassword = editTextUpdateMasterpassword.getText().toString();

            String deviceID;

            Log.d("BINDING", Boolean.toString(bindToDevice_enabled));

            if (bindToDevice_enabled) {
                deviceID = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Log.d("DEVICE ID", Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            } else {
                deviceID = "SECUSO";
            }

            //pack old parameters to String-Array
            String[] paramsOld = new String[12];
            paramsOld[0] = oldMetaData.getDOMAIN();
            paramsOld[1] = oldMetaData.getUSERNAME();
            paramsOld[2] = masterpassword;
            paramsOld[3] = deviceID;
            paramsOld[4] = String.valueOf(oldMetaData.getITERATION());
            paramsOld[5] = String.valueOf(number_iterations);
            paramsOld[6] = hashAlgorithm;
            paramsOld[7] = String.valueOf(oldMetaData.getHAS_SYMBOLS());
            paramsOld[8] = String.valueOf(oldMetaData.getHAS_LETTERS_LOW());
            paramsOld[9] = String.valueOf(oldMetaData.getHAS_LETTERS_UP());
            paramsOld[10] = String.valueOf(oldMetaData.getHAS_NUMBERS());
            paramsOld[11] = String.valueOf(oldMetaData.getLENGTH());

            new PasswordGeneratorTask() {
                @Override
                protected void onPostExecute(String result) {
                    textViewOldPassword.setText(result);
                    spinnerOld.setVisibility(View.GONE);
                }
            }.execute(paramsOld);

            Log.d("Generator Update Old", "Length: " + Integer.toString(oldMetaData.getLENGTH()));
            Log.d("Generator Update Old", "Domain: " + oldMetaData.getDOMAIN());
            Log.d("Generator Update Old", "Username: " + oldMetaData.getUSERNAME());
            Log.d("Generator Update Old", "Symbols: " + Integer.toString(oldMetaData.getHAS_SYMBOLS()));
            Log.d("Generator Update Old", "Lower: " + Integer.toString(oldMetaData.getHAS_LETTERS_LOW()));
            Log.d("Generator Update Old", "Upper: " + Integer.toString(oldMetaData.getHAS_LETTERS_UP()));
            Log.d("Generator Update Old", "Numbers: " + Integer.toString(oldMetaData.getHAS_NUMBERS()));
            Log.d("Generator Update Old", "Iterations: " + Integer.toString(oldMetaData.getITERATION()));

            //generate new password
            metaData = database.getMetaData(position);

            //pack new parameters to String-Array
            String[] paramsNew = new String[12];
            paramsNew[0] = metaData.getDOMAIN();
            paramsNew[1] = metaData.getUSERNAME();
            paramsNew[2] = masterpassword;
            paramsNew[3] = deviceID;
            paramsNew[4] = String.valueOf(metaData.getITERATION());
            paramsNew[5] = String.valueOf(number_iterations);
            paramsNew[6] = hashAlgorithm;
            paramsNew[7] = String.valueOf(metaData.getHAS_SYMBOLS());
            paramsNew[8] = String.valueOf(metaData.getHAS_LETTERS_LOW());
            paramsNew[9] = String.valueOf(metaData.getHAS_LETTERS_UP());
            paramsNew[10] = String.valueOf(metaData.getHAS_NUMBERS());
            paramsNew[11] = String.valueOf(metaData.getLENGTH());

            new PasswordGeneratorTask() {
                @Override
                protected void onPostExecute(String result) {
                    textViewNewPassword.setText(result);
                    spinnerNew.setVisibility(View.GONE);
                }
            }.execute(paramsNew);

            Log.d("Generator Update", "Length: " + Integer.toString(metaData.getLENGTH()));
            Log.d("Generator Update", "Domain: " + metaData.getDOMAIN());
            Log.d("Generator Update", "Username: " + metaData.getUSERNAME());
            Log.d("Generator Update", "Symbols: " + Integer.toString(metaData.getHAS_SYMBOLS()));
            Log.d("Generator Update", "Numbers: " + Integer.toString(metaData.getHAS_NUMBERS()));
            Log.d("Generator Update", "Lower: " + Integer.toString(metaData.getHAS_LETTERS_LOW()));
            Log.d("Generator Update", "Upper: " + Integer.toString(metaData.getHAS_LETTERS_UP()));
            Log.d("Generator Update", "Iterations: " + Integer.toString(metaData.getITERATION()));

        }
    }

    public void setOldMetaData(Bundle bundle) {
        oldMetaData = new MetaData(0, 0,
                bundle.getString("olddomain"),
                bundle.getString("oldusername"),
                bundle.getInt("oldlength"),
                bundle.getInt("oldnumbers"),
                bundle.getInt("oldsymbols"),
                bundle.getInt("oldlettersup"),
                bundle.getInt("oldletterslow"),
                bundle.getInt("olditeration")
        );

//        Log.d("Update Passwords", "olddomain: " + oldMetaData.getDOMAIN());
//        Log.d("Update Passwords", "oldlength: " + oldMetaData.getLENGTH());
//        Log.d("Update Passwords", "oldsymbols: " + oldMetaData.getHAS_SYMBOLS());
//        Log.d("Update Passwords", "oldnumbers: " + oldMetaData.getHAS_NUMBERS());
//        Log.d("Update Passwords", "olditeration: " + oldMetaData.getITERATION());

    }

}
