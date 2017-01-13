package org.secuso.privacyfriendlypasswordgenerator.dialogs;

/**
 * @author Karola Marky
 * @version 20161116
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;

public class AddMetaDataDialog extends DialogFragment {

    Activity activity;
    View rootView;
    MetaDataSQLiteHelper database;
    boolean closeDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_metadata, null);

        rootView = view;

        builder.setView(view);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.add_metadata_heading));

        database = MetaDataSQLiteHelper.getInstance(getActivity());

        //Seekbar
        SeekBar seekBarLength = (SeekBar) view.findViewById(R.id.seekBarLength);
        final TextView textViewLengthDisplayFinal = (TextView) view.findViewById(R.id.textViewLengthDisplay);
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewLengthDisplayFinal.setText(Integer.toString(progress + 4));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        builder.setPositiveButton(getActivity().getString(R.string.add), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                addMetaData();

            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.cancel), null);

        return builder.create();
    }

    public int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    public void addMetaData() {

        SeekBar seekBarLength = (SeekBar) rootView.findViewById(R.id.seekBarLength);
        CheckBox hasNumbersCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxNumbers);
        CheckBox hasSymbolsCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxSpecialCharacter);
        CheckBox hasLettersUpCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxLettersUp);
        CheckBox hasLettersLowCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxLettersLow);
        EditText domain = (EditText) rootView.findViewById(R.id.editTextDomain);
        EditText username = (EditText) rootView.findViewById(R.id.editTextUsername);
        EditText iterations = (EditText) rootView.findViewById(R.id.EditTextIteration);

        int iterationToAdd = 1;

        if (iterations.getText().toString().length() > 0) {
            iterationToAdd = Integer.parseInt(iterations.getText().toString());
        }
        if (domain.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(activity.getBaseContext(), getString(R.string.add_domain_message), Toast.LENGTH_SHORT);
            toast.show();
            closeDialog = false;
        } else if (!(hasNumbersCheckBox.isChecked() || hasSymbolsCheckBox.isChecked() || hasLettersUpCheckBox.isChecked() || hasLettersLowCheckBox.isChecked())) {
            Toast toast = Toast.makeText(activity.getBaseContext(), getString(R.string.add_character_message), Toast.LENGTH_SHORT);
            toast.show();
        } else {

            MetaData metaDataToAdd = new MetaData(0, 0,
                    domain.getText().toString(),
                    username.getText().toString(),
                    seekBarLength.getProgress() + 4,
                    boolToInt(hasNumbersCheckBox.isChecked()),
                    boolToInt(hasSymbolsCheckBox.isChecked()),
                    boolToInt(hasLettersUpCheckBox.isChecked()),
                    boolToInt(hasLettersLowCheckBox.isChecked()),
                    iterationToAdd);

            database.addMetaData(metaDataToAdd);

            activity.recreate();

            closeDialog = true;

        }


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
                    addMetaData();
                    if (closeDialog) {
                        dismiss();
                    }

                }
            });
        }
    }

}
