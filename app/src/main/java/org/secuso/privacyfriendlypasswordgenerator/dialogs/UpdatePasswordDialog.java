/**
 * This file is part of Privacy Friendly Password Generator.
 * <p>
 * Privacy Friendly Password Generator is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * <p>
 * Privacy Friendly Password Generator is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGeneratorTask;
import org.secuso.privacyfriendlypasswordgenerator.helpers.SaltHelper;

/**
 * @author Karola Marky
 * @version 20170113
 */

public class UpdatePasswordDialog extends DialogFragment {

    private View rootView;

    private MetaDataSQLiteHelper database;
    private int position;
    private MetaData metaData;
    private MetaData oldMetaData;

    private String hashAlgorithm;
    private int number_iterations;

    private ProgressBar spinnerOld;
    private ProgressBar spinnerNew;

    private ImageButton visibilityButton;
    private boolean visibility;

    private EditText editTextUpdateMasterpassword;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rootView = inflater.inflate(R.layout.dialog_update_passwords, null);

        visibility = false;

        spinnerOld = (ProgressBar) rootView.findViewById(R.id.oldProgressBar);
        spinnerOld.setVisibility(View.GONE);

        spinnerNew = (ProgressBar) rootView.findViewById(R.id.newProgressBar);
        spinnerNew.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        position = bundle.getInt("position");
        hashAlgorithm = bundle.getString("hash_algorithm");
        setOldMetaData(bundle);
        number_iterations = bundle.getInt("number_iterations");

        database = MetaDataSQLiteHelper.getInstance(getActivity());

        builder.setView(rootView);

        builder.setIcon(R.mipmap.ic_drawer);

        builder.setTitle(getActivity().getString(R.string.passwords_heading));

        Button displayButton = (Button) rootView.findViewById(R.id.displayButton);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", oldPassword.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getActivity().getString(R.string.copy_clipboar_old), Toast.LENGTH_SHORT).show();
            }
        });

        copyNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView newPassword = (TextView) rootView.findViewById(R.id.textViewNewPassword);
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", newPassword.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getActivity().getString(R.string.copy_clipboar_new), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton(getActivity().getString(R.string.done), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickDone();
            }
        });

        visibilityButton = (ImageButton) rootView.findViewById(R.id.visibilityButton);

        visibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextUpdateMasterpassword = (EditText) rootView.findViewById(R.id.editTextUpdateMasterpassword);

                if (!visibility) {
                    visibilityButton.setImageResource(R.drawable.ic_visibility_off);
                    editTextUpdateMasterpassword.setInputType(
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextUpdateMasterpassword.setSelection(editTextUpdateMasterpassword.getText().length());
                    visibility = true;
                } else {
                    visibilityButton.setImageResource(R.drawable.ic_visibility);
                    editTextUpdateMasterpassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextUpdateMasterpassword.setSelection(editTextUpdateMasterpassword.getText().length());
                    visibility = false;
                }
            }
        });

        return builder.create();
    }

    public void onClickDone() {
        getActivity().recreate();
        this.dismiss();
    }

    public void displayPasswords() {
        editTextUpdateMasterpassword = (EditText) rootView.findViewById(R.id.editTextUpdateMasterpassword);
        TextView textViewOld = (TextView) rootView.findViewById(R.id.textViewOldPassword);
        TextView textViewNew = (TextView) rootView.findViewById(R.id.textViewNewPassword);

        textViewOld.setText("");
        textViewNew.setText("");

        if (editTextUpdateMasterpassword.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(getActivity().getBaseContext(), getString(R.string.enter_masterpassword), Toast.LENGTH_SHORT);
            toast.show();
        } else if (editTextUpdateMasterpassword.getText().toString().length() < 8) {
            Toast toast = Toast.makeText(getActivity().getBaseContext(), getString(R.string.masterpassword_length), Toast.LENGTH_SHORT);
            toast.show();
        } else {

            spinnerOld.setVisibility(View.VISIBLE);
            spinnerNew.setVisibility(View.VISIBLE);

            final TextView textViewOldPassword = (TextView) rootView.findViewById(R.id.textViewOldPassword);
            final TextView textViewNewPassword = (TextView) rootView.findViewById(R.id.textViewNewPassword);

            String deviceID = SaltHelper.getSalt(requireActivity().getBaseContext());

            PasswordGeneratorTask.PasswordGeneratorParameter paramsOld = new PasswordGeneratorTask.PasswordGeneratorParameter(
                    oldMetaData.getDOMAIN(),
                    oldMetaData.getUSERNAME(),
                    editTextUpdateMasterpassword.getText().toString(),
                    deviceID,
                    oldMetaData.getITERATION(),
                    number_iterations,
                    hashAlgorithm,
                    oldMetaData.getHAS_SYMBOLS(),
                    oldMetaData.getHAS_LETTERS_LOW(),
                    oldMetaData.getHAS_LETTERS_UP(),
                    oldMetaData.getHAS_NUMBERS(),
                    oldMetaData.getLENGTH()
            );

            new PasswordGeneratorTask() {
                @Override
                protected void onPostExecute(String result) {
                    textViewOldPassword.setText(result);
                    spinnerOld.setVisibility(View.GONE);
                }
            }.execute(paramsOld);

            //generate new password
            metaData = database.getMetaData(position);

            PasswordGeneratorTask.PasswordGeneratorParameter paramsNew = new PasswordGeneratorTask.PasswordGeneratorParameter(
                    metaData.getDOMAIN(),
                    metaData.getUSERNAME(),
                    editTextUpdateMasterpassword.getText().toString(),
                    deviceID,
                    metaData.getITERATION(),
                    number_iterations,
                    hashAlgorithm,
                    metaData.getHAS_SYMBOLS(),
                    metaData.getHAS_LETTERS_LOW(),
                    metaData.getHAS_LETTERS_UP(),
                    metaData.getHAS_NUMBERS(),
                    metaData.getLENGTH()
            );

            new PasswordGeneratorTask() {
                @Override
                protected void onPostExecute(String result) {
                    textViewNewPassword.setText(result);
                    spinnerNew.setVisibility(View.GONE);
                }
            }.execute(paramsNew);

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

    }

}
