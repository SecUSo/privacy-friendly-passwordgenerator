/**
 * This file is part of Privacy Friendly Password Generator.

 Privacy Friendly Password Generator is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly Password Generator is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;

/**
 * @author Karola Marky
 * @version 20170113
 */

public class UpdateMetadataDialog extends DialogFragment {

    private View rootView;

    private MetaDataSQLiteHelper database;

    private int position;

    private MetaData metaData;
    private MetaData oldMetaData;

    private String hash_algorithm;
    private boolean bindToDevice_enabled;
    private int number_iterations;

    private boolean closeDialog;
    private boolean versionVisible;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rootView = inflater.inflate(R.layout.dialog_update_metadata, null);

        versionVisible = false;

        Bundle bundle = getArguments();

        position = bundle.getInt("position");
        hash_algorithm = bundle.getString("hash_algorithm");
        bindToDevice_enabled = bundle.getBoolean("bindToDevice_enabled");


        database = MetaDataSQLiteHelper.getInstance(getActivity());
        metaData = database.getMetaData(position);
        oldMetaData = database.getMetaData(position);
        number_iterations = bundle.getInt("number_iterations");

        builder.setView(rootView);
        setUpData();

        builder.setIcon(R.mipmap.ic_drawer);

        builder.setTitle(getActivity().getString(R.string.add_new_metadata_heading));

        builder.setPositiveButton(getActivity().getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                updateMetadata(oldMetaData.getITERATION());

            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                cancelUpdate();

            }
        });

        Button versionButton = (Button) rootView.findViewById(R.id.versionButton);
        versionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout versionDataLayout = (RelativeLayout) rootView.findViewById(R.id.updateVersionLayout);
                TextView versionTextView = (TextView) rootView.findViewById(R.id.versionButton);
                TextView textViewIteration = (TextView) rootView.findViewById(R.id.textViewIteration);
                if (!versionVisible) {
                    versionDataLayout.setVisibility(View.VISIBLE);
                    textViewIteration.setVisibility(View.VISIBLE);
                    versionTextView.setText(getString(R.string.change_version_opened));
                    versionTextView.setTextColor(Color.BLACK);
                    versionVisible = true;
                } else {
                    versionDataLayout.setVisibility(View.GONE);
                    textViewIteration.setVisibility(View.GONE);
                    versionTextView.setText(getString(R.string.change_version_closed));
                    versionTextView.setTextColor(Color.parseColor("#d3d3d3"));
                    versionVisible = false;
                }

            }

        });

        ImageButton versionInfoImageButton = (ImageButton) rootView.findViewById(R.id.versionInfoImageButton);
        versionInfoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder infoBbuilder = new AlertDialog.Builder(getActivity());
                infoBbuilder.setTitle(getString(R.string.dialog_version_title));
                infoBbuilder.setMessage(R.string.dialog_version);
                infoBbuilder.show();
            }

        });

        return builder.create();
    }

    public void setCheckBox(CheckBox checkbox, int value) {
        if (value == 1) {
            checkbox.setChecked(true);
        }
    }

    /**
     * Displays old metadata and lets user add new metadata
     */
    public void setUpData() {
        EditText domain = (EditText) rootView.findViewById(R.id.editTextDomainUpdate);
        EditText username = (EditText) rootView.findViewById(R.id.editTextUsernameUpdate);
        TextView oldVersion = (TextView) rootView.findViewById(R.id.textViewIteration);
        EditText newVersion = (EditText) rootView.findViewById(R.id.EditTextIteration);

        CheckBox checkBoxSpecialCharacterUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxSpecialCharacterUpdate);
        CheckBox checkBoxLettersLowUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxLettersLowUpdate);
        CheckBox checkBoxLettersUpUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxLettersUpUpdate);
        CheckBox checkBoxNumbersUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxNumbersUpdate);

        setCheckBox(checkBoxSpecialCharacterUpdate, metaData.getHAS_SYMBOLS());
        setCheckBox(checkBoxLettersLowUpdate, metaData.getHAS_LETTERS_LOW());
        setCheckBox(checkBoxLettersUpUpdate, metaData.getHAS_LETTERS_UP());
        setCheckBox(checkBoxNumbersUpdate, metaData.getHAS_NUMBERS());

        domain.setText(metaData.getDOMAIN());
        username.setText(metaData.getUSERNAME());

        oldVersion.setText(getString(R.string.old_version, String.valueOf(metaData.getITERATION())));
        newVersion.setText(String.valueOf(metaData.getITERATION() + 1));

        TextView textViewLengthDisplayUpdate = (TextView) rootView.findViewById(R.id.textViewLengthDisplayUpdate);
        textViewLengthDisplayUpdate.setText(Integer.toString(metaData.getLENGTH()));

        final TextView finalTextViewLengthDisplayUpdate = textViewLengthDisplayUpdate;

        SeekBar seekBarLength = (SeekBar) rootView.findViewById(R.id.seekBarLengthUpdate);
        seekBarLength.setProgress(metaData.getLENGTH() - 4);

        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                finalTextViewLengthDisplayUpdate.setText(Integer.toString(progress + 4));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public void updateMetadata(int oldIteration) {

        SeekBar seekBarLength = (SeekBar) rootView.findViewById(R.id.seekBarLengthUpdate);
        CheckBox hasNumbersCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxNumbersUpdate);
        CheckBox hasSymbolsCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxSpecialCharacterUpdate);
        CheckBox checkBoxLettersLowUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxLettersLowUpdate);
        CheckBox checkBoxLettersUpUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxLettersUpUpdate);
        EditText domain = (EditText) rootView.findViewById(R.id.editTextDomainUpdate);
        EditText username = (EditText) rootView.findViewById(R.id.editTextUsernameUpdate);
        EditText iteration = (EditText) rootView.findViewById(R.id.EditTextIteration);

        if (domain.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(getActivity().getBaseContext(), getString(R.string.add_domain_message), Toast.LENGTH_SHORT);
            toast.show();
            closeDialog = false;
        } else if (!(hasNumbersCheckBox.isChecked() || hasSymbolsCheckBox.isChecked() || checkBoxLettersUpUpdate.isChecked() || checkBoxLettersLowUpdate.isChecked())) {
            Toast toast = Toast.makeText(getActivity().getBaseContext(), getString(R.string.add_character_message), Toast.LENGTH_SHORT);
            toast.show();
        } else {

            int tempIteration;

            if (iteration.getText().length() == 0) {
                tempIteration = oldIteration + 1;
            } else {
                tempIteration = Integer.parseInt(iteration.getText().toString());
            }
            database.updateMetaData(

                    new MetaData(position, position,
                            domain.getText().toString(),
                            username.getText().toString(),
                            seekBarLength.getProgress() + 4,
                            boolToInt(hasNumbersCheckBox.isChecked()),
                            boolToInt(hasSymbolsCheckBox.isChecked()),
                            boolToInt(checkBoxLettersUpUpdate.isChecked()),
                            boolToInt(checkBoxLettersLowUpdate.isChecked()),
                            tempIteration));

            Toast.makeText(getActivity(), getString(R.string.added_message), Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("hash_algorithm", hash_algorithm);
            bundle.putInt("number_iterations", number_iterations);
            bundle.putBoolean("bindToDevice_enabled", bindToDevice_enabled);
            bundle.putString("olddomain", oldMetaData.getDOMAIN());
            bundle.putString("oldusername", oldMetaData.getUSERNAME());

            bundle.putInt("oldlength", oldMetaData.getLENGTH());
            bundle.putInt("oldlettersup", oldMetaData.getHAS_LETTERS_UP());
            bundle.putInt("oldletterslow", oldMetaData.getHAS_LETTERS_LOW());
            bundle.putInt("oldsymbols", oldMetaData.getHAS_SYMBOLS());
            bundle.putInt("oldnumbers", oldMetaData.getHAS_NUMBERS());
            bundle.putInt("olditeration", oldMetaData.getITERATION());
            FragmentManager fragmentManager = getFragmentManager();
            UpdatePasswordDialog updatePasswordDialog = new UpdatePasswordDialog();
            updatePasswordDialog.setArguments(bundle);
            updatePasswordDialog.show(fragmentManager, "UpdatePasswordDialog");

            closeDialog = true;
        }
    }

    public void cancelUpdate() {

        Toast.makeText(getActivity(), getString(R.string.canceled_message), Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    public int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMetadata(oldMetaData.getITERATION());
                    if (closeDialog) {
                        dismiss();
                    }

                }
            });
        }
    }

}

