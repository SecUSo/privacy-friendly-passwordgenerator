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

/**
 * @author Karola Marky
 * @version 20161212
 */

public class TemplateFactory {

    public static String createTemplateFromParameters(int specialCharacters, int lowerCaseLetters, int upperCaseLetters,
                                 int numbers, int length){

        int lengthCount = 0;
        String template = "";
        if (specialCharacters == 1) {
            template += "s";
            lengthCount ++;
        }
        if (lowerCaseLetters == 1) {
            template += "a";
            lengthCount ++;
        }
        if (upperCaseLetters == 1) {
            template += "A";
            lengthCount ++;
        }
        if (numbers == 1) {
            template += "n";
            lengthCount ++;
        }

        for (int i=lengthCount; i<length; i++){
            template += "x";
        }

        return template;
    }

}
