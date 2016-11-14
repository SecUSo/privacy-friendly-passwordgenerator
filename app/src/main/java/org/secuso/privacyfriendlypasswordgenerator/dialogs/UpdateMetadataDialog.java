package org.secuso.privacyfriendlypasswordgenerator.dialogs;

/**
 * Created by karo on 14.11.16.
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;

/**
 * Created by karo on 13.11.16.
 */

public class UpdateMetadataDialog extends DialogFragment {

    Activity activity;
    View rootView;
    MetaDataSQLiteHelper database;
    int position;
    MetaData metaData;
    MetaData oldMetaData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        rootView = inflater.inflate(R.layout.dialog_update_metadata, null);

        Bundle bundle = getArguments();

        if (bundle != null) {
            position = bundle.getInt("position");
        } else {
            position = -1;
        }

        this.database = new MetaDataSQLiteHelper(getActivity());
        metaData = database.getMetaData(position);
        oldMetaData = database.getMetaData(position);

        builder.setView(rootView);
        setUpData();

        builder.setIcon(R.mipmap.ic_drawer);

        builder.setTitle(getActivity().getString(R.string.add_new_metadata_heading));

        builder.setPositiveButton(getActivity().getString(R.string.next), new DialogInterface.OnClickListener() {

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

        return builder.create();
    }

    public void setCheckBox(CheckBox checkbox, int value) {
        if (value == 1) {
            checkbox.isChecked();
        }
    }

    /**
     * Displays old metadata and lets user add new metadata
     */
    public void setUpData() {
        EditText domain = (EditText) rootView.findViewById(R.id.editTextDomainUpdate);

        CheckBox checkBoxSpecialCharacterUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxSpecialCharacterUpdate);
        CheckBox checkBoxLettersUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxLettersUpdate);
        CheckBox checkBoxNumbersUpdate = (CheckBox) rootView.findViewById(R.id.checkBoxNumbersUpdate);

        setCheckBox(checkBoxSpecialCharacterUpdate, metaData.getHAS_SYMBOLS());
        setCheckBox(checkBoxLettersUpdate, metaData.getHAS_LETTERS());
        setCheckBox(checkBoxNumbersUpdate, metaData.getHAS_NUMBERS());

        domain.setText(metaData.getDOMAIN());

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

    //TODO Find out best point of time to update metadata
    public void updateMetadata(int oldIteration) {

        SeekBar seekBarLength = (SeekBar) rootView.findViewById(R.id.seekBarLengthUpdate);
        CheckBox hasNumbersCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxNumbersUpdate);
        CheckBox hasSymbolsCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxSpecialCharacterUpdate);
        CheckBox hasLettersCheckBox = (CheckBox) rootView.findViewById(R.id.checkBoxLettersUpdate);
        EditText domain = (EditText) rootView.findViewById(R.id.editTextDomainUpdate);

        database.updateMetaData(
                new MetaData(position, position,
                        domain.getText().toString(),
                        seekBarLength.getProgress() + 4,
                        boolToInt(hasNumbersCheckBox.isChecked()),
                        boolToInt(hasSymbolsCheckBox.isChecked()),
                        boolToInt(hasLettersCheckBox.isChecked()),
                        oldIteration + 1));

        Toast.makeText(activity, getString(R.string.added_message), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("olddomain", oldMetaData.getDOMAIN());

//        Log.d("Update Metadata", "olddomain: " + oldMetaData.getDOMAIN());
//        Log.d("Update Metadata", "oldlength: " + oldMetaData.getLENGTH());
//        Log.d("Update Metadata", "oldletters: " + oldMetaData.getHAS_LETTERS());
//        Log.d("Update Metadata", "oldsymbols: " + oldMetaData.getHAS_SYMBOLS());
//        Log.d("Update Metadata", "oldnumbers: " + oldMetaData.getHAS_NUMBERS());
//        Log.d("Update Metadata", "olditeration: " + oldMetaData.getITERATION());

        bundle.putInt("oldlength", oldMetaData.getLENGTH());
        bundle.putInt("oldletters", oldMetaData.getHAS_LETTERS());
        bundle.putInt("oldsymbols", oldMetaData.getHAS_SYMBOLS());
        bundle.putInt("oldnumbers", oldMetaData.getHAS_NUMBERS());
        bundle.putInt("olditeration", oldMetaData.getITERATION());
        FragmentManager fragmentManager = getFragmentManager();
        UpdatePasswordDialog updatePasswordDialog = new UpdatePasswordDialog();
        updatePasswordDialog.setArguments(bundle);
        updatePasswordDialog.show(fragmentManager, "UpdatePasswordDialog");
    }

    public void cancelUpdate() {

        Toast.makeText(activity, getString(R.string.canceled_message), Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    public int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

}

