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

package org.secuso.privacyfriendlypasswordgenerator.generator;

import android.os.AsyncTask;

/**
 * @author Karola Marky
 * @version 20161126
 */

public class PasswordGeneratorTask extends AsyncTask<PasswordGeneratorTask.PasswordGeneratorParameter, Void, String> {

    public static class PasswordGeneratorParameter {
        private final String domain;
        private final String username;
        private final String masterpassword;
        private final String deviceID;
        private final int iteration;
        private final int hashIterations;
        private final String hashAlgorithm;
        private final int specialCharacters, lowerCaseLetters, upperCaseLetters, numbers, length;

        public PasswordGeneratorParameter(String domain,
                                          String username,
                                          String masterpassword,
                                          String deviceID,
                                          int iteration,
                                          int hashIterations,
                                          String hashAlgorithm,
                                          int specialCharacters,
                                          int lowerCaseLetters,
                                          int upperCaseLetters,
                                          int numbers,
                                          int length) {
            this.domain = domain;
            this.username = username;
            this.masterpassword = masterpassword;
            this.deviceID = deviceID;
            this.iteration = iteration;
            this.hashIterations = hashIterations;
            this.hashAlgorithm = hashAlgorithm;
            this.specialCharacters = specialCharacters;
            this.lowerCaseLetters = lowerCaseLetters;
            this.upperCaseLetters = upperCaseLetters;
            this.numbers = numbers;
            this.length = length;
        }
    }


    @Override
    protected String doInBackground(PasswordGeneratorParameter... parameters) {
        PasswordGeneratorParameter parameter = parameters[0];

        PasswordGenerator generator = new PasswordGenerator(parameter.domain,
                parameter.username,
                parameter.masterpassword,
                parameter.deviceID,
                parameter.iteration,
                parameter.hashIterations,
                parameter.hashAlgorithm);

        String password =
                generator.getPassword(
                        parameter.specialCharacters,
                        parameter.lowerCaseLetters,
                        parameter.upperCaseLetters,
                        parameter.numbers,
                        parameter.length);
        try {
            generator.deleteFinalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return password;
    }
}
