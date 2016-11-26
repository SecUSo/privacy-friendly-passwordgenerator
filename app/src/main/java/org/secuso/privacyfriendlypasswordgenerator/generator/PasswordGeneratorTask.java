package org.secuso.privacyfriendlypasswordgenerator.generator;

import android.os.AsyncTask;

import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;
import org.secuso.privacyfriendlypasswordgenerator.generator.UTF8;

/**
 * Created by karo on 26.11.16.
 */

public class PasswordGeneratorTask extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... strings) {

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
}
