package org.secuso.privacyfriendlypasswordgenerator.generator;

import android.os.AsyncTask;

/**
 * Created by karo on 26.11.16.
 */

public class PasswordGeneratorTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {

        PasswordGenerator generator = new PasswordGenerator(strings[0],
                strings[1],
                strings[2],
                strings[3],
                Integer.valueOf(strings[4]),
                Integer.parseInt(strings[5]),
                strings[6]);
        String password = generator.getPassword(Integer.parseInt(strings[7]), Integer.parseInt(strings[8]), Integer.parseInt(strings[9]), Integer.parseInt(strings[10]), Integer.parseInt(strings[11]));
        try {
            generator.deleteFinalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return password;
    }
}
