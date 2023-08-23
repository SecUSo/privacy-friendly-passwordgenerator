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

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGeneratorTask;

/**
 * @author Karola Marky
 * @version 20170113
 */

public class BenchmarkDialog extends DialogFragment {

    private View rootView;

    private int iterations;
    private String hashAlgorithm;
    private String bcryptCost;

    private ProgressBar spinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = inflater.inflate(R.layout.dialog_benchmark, null);

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        hashAlgorithm = bundle.getString("hash_algorithm");
        iterations = bundle.getInt("number_iterations");
        bcryptCost = bundle.getString("bcrypt_cost");

        builder.setView(rootView);
        builder.setIcon(R.mipmap.ic_drawer);
        builder.setTitle(getActivity().getString(R.string.dialog_benchmark_title));
        builder.setPositiveButton(getActivity().getString(R.string.done), null);

        final Button benchmarkButton = (Button) rootView.findViewById(R.id.benchmarkButton);
        benchmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView executionTextView = (TextView) rootView.findViewById(R.id.benchmark_execution);
                executionTextView.setText(getString(R.string.dialog_benchmark_time));

                TextView benchmarkTextView = (TextView) rootView.findViewById(R.id.benchmarkTextView);
                benchmarkTextView.setText("");

                spinner.setVisibility(View.VISIBLE);

                generatePassword(iterations, hashAlgorithm, bcryptCost);
            }
        });

        return builder.create();
    }

    public void generatePassword(int iterations, String hashFunction, String bcryptCost) {

        final double startTime = System.currentTimeMillis();

        PasswordGeneratorTask.PasswordGeneratorParameter params = new PasswordGeneratorTask.PasswordGeneratorParameter(
                "abc.com",
                "user",
                "masterpassword",
                "deviceID",
                4242,
                iterations,
                hashFunction,
                bcryptCost,
                1,
                1,
                1,
                1,
                20
        );

        new PasswordGeneratorTask() {
            @Override
            protected void onPostExecute(String result) {
                TextView benchmarkTextView = (TextView) rootView.findViewById(R.id.benchmarkTextView);
                double stopTime = System.currentTimeMillis();
                spinner.setVisibility(View.GONE);
                benchmarkTextView.setText(String.valueOf((stopTime - startTime) / 1000) + " " + getString(R.string.dialog_benchmark_seconds));
            }
        }.execute(params);
    }
}



