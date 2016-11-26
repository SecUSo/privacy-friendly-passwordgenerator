package org.secuso.privacyfriendlypasswordgenerator.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;
import org.secuso.privacyfriendlypasswordgenerator.generator.UTF8;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by karo on 13.11.16.
 */

public class GeneratePasswordDialog extends DialogFragment {

    Activity activity;
    View rootView;
    MetaDataSQLiteHelper database;
    int position;
    MetaData metaData;
    Boolean bindToDevice_enabled;
    Boolean clipboard_enabled;
    String hashAlgorithm;
    int number_iterations;

    ProgressBar spinner;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = inflater.inflate(R.layout.dialog_generate_password, null);

        Bundle bundle = getArguments();

        position = bundle.getInt("position");
        clipboard_enabled = bundle.getBoolean("clipboard_enabled");
        bindToDevice_enabled = bundle.getBoolean("bindToDevice_enabled");
        hashAlgorithm = bundle.getString("hash_algorithm");
        number_iterations = bundle.getInt("number_iterations");

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        this.database = new MetaDataSQLiteHelper(getActivity());
        metaData = database.getMetaData(position);

        TextView domain = (TextView) rootView.findViewById(R.id.domainHeadingTextView);
        domain.setText(metaData.getDOMAIN());

        TextView username = (TextView) rootView.findViewById(R.id.domainUsernameTextView);

        username.setText(metaData.getUSERNAME());

        TextView iteration = (TextView) rootView.findViewById(R.id.textViewIteration);
        iteration.setText(String.valueOf(metaData.getITERATION()));

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.generate_heading));
        builder.setPositiveButton(getActivity().getString(R.string.done), null);

        Button generateButton = (Button) rootView.findViewById(R.id.generatorButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView textViewPassword = (TextView) rootView.findViewById(R.id.textViewPassword);
                textViewPassword.setText("");

                InputMethodManager inputManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                EditText editTextMasterpassword = (EditText) rootView.findViewById(R.id.editTextMasterpassword);

                if (editTextMasterpassword.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(activity.getBaseContext(), getString(R.string.enter_masterpassword), Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    spinner.setVisibility(View.VISIBLE);

                    generatePassword();
                }
            }
        });

        return builder.create();
    }

    public void generatePassword() {

        EditText editTextMasterpassword = (EditText) rootView.findViewById(R.id.editTextMasterpassword);

        metaData = database.getMetaData(position);

        Log.d("BINDING", Boolean.toString(bindToDevice_enabled));

        String deviceID;
        if (bindToDevice_enabled) {
            deviceID = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.d("DEVICE ID", Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
        } else {
            deviceID = "SECUSO";
        }

        //pack parameters to String-Array
        String[] params = new String[12];
        params[0] = metaData.getDOMAIN();
        params[1] = metaData.getUSERNAME();
        params[2] = editTextMasterpassword.getText().toString();
        params[3] = deviceID;
        params[4] = String.valueOf(metaData.getITERATION());
        params[5] = String.valueOf(number_iterations);
        params[6] = hashAlgorithm;
        params[7] = String.valueOf(metaData.getHAS_SYMBOLS());
        params[8] = String.valueOf(metaData.getHAS_LETTERS_LOW());
        params[9] = String.valueOf(metaData.getHAS_LETTERS_UP());
        params[10] = String.valueOf(metaData.getHAS_NUMBERS());
        params[11] = String.valueOf(metaData.getLENGTH());

        PasswortGeneratorTask passwortGeneratorTask = new PasswortGeneratorTask();
        passwortGeneratorTask.execute(params);
    }

    public void passwordToClipboard(boolean clipboardEnabled, String password) {
        if (clipboardEnabled) {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(activity, activity.getString(R.string.password_copied), Toast.LENGTH_SHORT).show();
        }
    }

    public class PasswortGeneratorTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] strings) {

            PasswordGenerator generator = new PasswordGenerator(strings[0],
                    strings[1],
                    strings[2],
                    strings[3],
                    UTF8.encode(String.valueOf(strings[0])),
                    Integer.valueOf(strings[4]),
                    Integer.parseInt(strings[5]),
                    strings[6]);

            return generator.getPassword(Integer.parseInt(strings[7]), Integer.parseInt(strings[8]), Integer.parseInt(strings[9]), Integer.parseInt(strings[10]), Integer.parseInt(strings[11]));
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textViewPassword = (TextView) rootView.findViewById(R.id.textViewPassword);
            textViewPassword.setText(result);

            passwordToClipboard(clipboard_enabled, result);

            spinner.setVisibility(View.GONE);
        }

    }

}
