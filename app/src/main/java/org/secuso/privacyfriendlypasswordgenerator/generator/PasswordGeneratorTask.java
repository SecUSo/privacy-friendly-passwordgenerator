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

package org.secuso.privacyfriendlypasswordgenerator.generator;

import android.os.AsyncTask;

/**
 * @author Karola Marky
 * @version 20161126
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

        String password =
                generator.getPassword(
                        Integer.parseInt(strings[7]),
                        Integer.parseInt(strings[8]),
                        Integer.parseInt(strings[9]),
                        Integer.parseInt(strings[10]),
                        Integer.parseInt(strings[11]));
        try {
            generator.deleteFinalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return password;
    }
}
