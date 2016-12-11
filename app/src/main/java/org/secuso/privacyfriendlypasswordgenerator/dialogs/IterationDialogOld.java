package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import org.secuso.privacyfriendlypasswordgenerator.R;

/**
 * Created by karo on 29.11.16.
 */

public class IterationDialog extends DialogFragment{

    Activity activity;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = inflater.inflate(R.layout.dialog_iteration, null);

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.add_metadata_heading));

        Bundle bundle = this.getArguments();
        int current = bundle.getInt("number_iterations_invisible");

        final NumberPicker iterationsPicker = (NumberPicker) rootView.findViewById(R.id.iterationsPicker);
        iterationsPicker.setMinValue(1000);
        iterationsPicker.setMaxValue(99999);
        iterationsPicker.setValue(current);


        builder.setPositiveButton(getActivity().getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences preferences = activity.getApplicationContext().getSharedPreferences("main_preferences", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("number_iterations_invisible", String.valueOf(iterationsPicker.getValue()));
                editor.commit();
                //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
                //SharedPreferences.Editor editor = settings.edit();
//                SharedPreferences.Editor editor = getActivity().getPreferences().edit();
//                editor.putString("number_iterations", String.valueOf(iterationsPicker.getValue()));
//                Log.d("NUMBERPICKER", String.valueOf(iterationsPicker.getValue()));
//                editor.commit();

            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.cancel), null);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

}
