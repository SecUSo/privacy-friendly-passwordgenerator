package org.secuso.privacyfriendlypasswordgenerator.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;
import org.secuso.privacyfriendlypasswordgenerator.generator.UTF8;

/**
 * Created by karo on 23.11.16.
 */

public class BenchmarkDialog extends DialogFragment {

    Activity activity;
    View rootView;

    int iterations;
    String hashAlgorithm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = inflater.inflate(R.layout.dialog_benchmark, null);

        Bundle bundle = getArguments();

        hashAlgorithm = bundle.getString("hash_algorithm");
        iterations = bundle.getInt("number_iterations");

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.dialog_benchmark_title));
        builder.setPositiveButton(getActivity().getString(R.string.done), null);

        final Button benchmarkButton = (Button) rootView.findViewById(R.id.benchmarkButton);
        benchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //generatePassword(iterations, hashAlgorithm);
                TextView benchmarkTextView = (TextView) rootView.findViewById(R.id.benchmarkTextView);
                benchmarkTextView.setText(String.valueOf(generatePassword(iterations, hashAlgorithm)));
            }
        });

        return builder.create();
    }

    public double generatePassword(int iterations, String hash) {

        double startTime = System.currentTimeMillis();

        PasswordGenerator generator =
                new PasswordGenerator(
                        "abc.com",
                        "user",
                        "masterpassword",
                        "deviceID",
                        UTF8.encode("Salt"),
                        10,
                        iterations,
                        hash);
        generator.getPassword(1, 1, 1, 1, 20);

        double stopTime = System.currentTimeMillis();

        return (stopTime - startTime) / 1000;
    }


}
