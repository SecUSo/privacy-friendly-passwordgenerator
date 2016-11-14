package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;

/**
 * Created by karo on 13.11.16.
 */

public class GeneratePasswordDialog extends DialogFragment {

    Activity activity;
    View rootView;
    MetaDataSQLiteHelper database;
    int position;
    MetaData metaData;

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

        if (bundle != null) {
            position = bundle.getInt("position") + 1;
        } else {
            position = -1;
        }

        this.database = new MetaDataSQLiteHelper(getActivity());
        metaData = database.getMetaData(position);
        TextView domain = (TextView) rootView.findViewById(R.id.domainHeadingTextView);

        domain.setText(metaData.getDOMAIN());

        TextView iteration = (TextView) rootView.findViewById(R.id.textViewIteration);
        iteration.setText(Integer.toString(metaData.getITERATION()));

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.generate_heading));
        builder.setPositiveButton(getActivity().getString(R.string.cancel), null);

        Button generateButton = (Button) rootView.findViewById(R.id.generatorButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePassword();
            }
        });

        return builder.create();
    }

    public void generatePassword() {

        EditText editTextMasterpassword = (EditText) rootView.findViewById(R.id.editTextMasterpassword);

        if (editTextMasterpassword.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(activity.getBaseContext(), getString(R.string.enter_masterpassword), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Log.d("Generator"," Generator Button pressed");

            if (position < 0) {
                Toast toast = Toast.makeText(activity.getBaseContext(), "PROBLEM", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                metaData = database.getMetaData(position);
                Log.d("Generator", "Position: " + Integer.toString(position));

                PasswordGenerator generator = new PasswordGenerator();
                generator.initialize(
                        metaData.getDOMAIN(), editTextMasterpassword.getText().toString());
                generator.hash(metaData.getLENGTH());

                Log.d("Generator", "initialized");
                Log.d("Generator", "Length: " + Integer.toString(metaData.getLENGTH()));
                Log.d("Generator", "Domain: " + metaData.getDOMAIN());

                String password = generator.getPassword(metaData.getHAS_SYMBOLS(), metaData.getHAS_LETTERS(), metaData.getHAS_NUMBERS(), metaData.getLENGTH());

                Log.d("Generator", "Symbols: " + Integer.toString(metaData.getHAS_SYMBOLS()));
                Log.d("Generator", "Letters: " + Integer.toString(metaData.getHAS_LETTERS()));
                Log.d("Generator", "Numbers: " + Integer.toString(metaData.getHAS_NUMBERS()));

                TextView textViewPassword = (TextView) rootView.findViewById(R.id.textViewPassword);
                textViewPassword.setText(password);
                Log.d("Generator", password);
            }

        }

    }

}
