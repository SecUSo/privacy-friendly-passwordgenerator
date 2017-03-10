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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the hashing and the creation of passwords. Please initialize first.
 * Do not forget to hash at least once with PBKDF2 because otherwise the password might look not very
 * random. It is safe to hash often because an attacker has to hash as often as you did for
 * every try of a brute-force attack. getPassword creates a password string out of the hash
 * digest.
 * <p>
 * Basic class structure and idea taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 * Added the BCrypt component and ShuffleString
 *
 * @author Karola Marky
 * @version 20161116
 */
public class PasswordGenerator {

    private byte[] hashValue;

    public PasswordGenerator(String domain,
                             String username,
                             String masterpassword,
                             String deviceID,
                             int iteration,
                             int hashIterations,
                             String hashAlgorithm) {

        String temp = Base64.encode_base64(
                PBKDF2.hmac(
                        hashAlgorithm,
                        UTF8.encode(masterpassword),
                        UTF8.encode(String.valueOf(iteration * 100) + domain + username + deviceID),
                        hashIterations),
                22);

        this.hashValue = transformPassword(BCrypt.hashpw(masterpassword, "$2a$10$" + temp));
    }

    //cuts the salt from the password
    private byte[] transformPassword(String password) {
        byte[] passwordChar = UTF8.encode(password);
        byte[] transformedPassword = new byte[31];


        for (int i = 29; i < passwordChar.length; i++) {
            transformedPassword[i - 29] = passwordChar[i];
        }

        return transformedPassword;

    }

    public String getPassword(int specialCharacters, int lowerCaseLetters, int upperCaseLetters,
                              int numbers, int length) {

        byte[] positiveHashValue = new byte[hashValue.length + 1];
        positiveHashValue[0] = 0;
        System.arraycopy(hashValue, 0, positiveHashValue, 1, hashValue.length);
        BigInteger hashNumber = new BigInteger(positiveHashValue);
        Clearer.zero(positiveHashValue);
        String password = "";

        String upperInitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerInitial = "abcdefghijklmnopqrstuvwxyz";
        String characters = "#!\"~|@^°$%&/()[]{}=-_+*<>;:.";
        String numbersInitial = "0123456789";

        List<String> characterSet = new ArrayList<>();

        if (specialCharacters == 1) {
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (lowerCaseLetters == 1) {
            for (int i = 0; i < lowerInitial.length(); i++) {
                characterSet.add(Character.toString(lowerInitial.charAt(i)));
            }
        }

        if (upperCaseLetters == 1) {
            for (int i = 0; i < upperInitals.length(); i++) {
                characterSet.add(Character.toString(upperInitals.charAt(i)));
            }

        }

        if (numbers == 1) {
            for (int i = 0; i < numbersInitial.length(); i++) {
                characterSet.add(Character.toString(numbersInitial.charAt(i)));
            }
        }

        List<String> digitsSet = new ArrayList<>();
        for (int i = 0; i < numbersInitial.length(); i++) {
            digitsSet.add(Character.toString(numbersInitial.charAt(i)));
        }

        List<String> lowerSet = new ArrayList<>();
        for (int i = 0; i < lowerInitial.length(); i++) {
            lowerSet.add(Character.toString(lowerInitial.charAt(i)));
        }

        List<String> upperSet = new ArrayList<>();
        for (int i = 0; i < upperInitals.length(); i++) {
            upperSet.add(Character.toString(upperInitals.charAt(i)));
        }

        List<String> extraSet = new ArrayList<>();
        for (int i = 0; i < characters.length(); i++) {
            extraSet.add(Character.toString(characters.charAt(i)));
        }


        if (characterSet.size() > 0) {
            String template = shuffleTemplate(TemplateFactory.createTemplateFromParameters(specialCharacters, lowerCaseLetters, upperCaseLetters,
            numbers, length));

            if (characterSet.size() > 0) {
                for (int i = 0; i < template.length(); i++) {
                    if (hashNumber.compareTo(BigInteger.ZERO) > 0) {
                        List<String> set = characterSet;
                        if (template.charAt(i) == 'a') {
                            set = lowerSet;
                        } else if (template.charAt(i) == 'A') {
                            set = upperSet;
                        } else if (template.charAt(i) == 'n') {
                            set = digitsSet;
                        } else if (template.charAt(i) == 's') {
                            set = extraSet;
                        } else if (template.charAt(i) == 'x') {
                            set = characterSet;
                        }
                        BigInteger setSize = BigInteger.valueOf(set.size());
                        BigInteger[] divAndMod = hashNumber.divideAndRemainder(setSize);
                        hashNumber = divAndMod[0];
                        int mod = divAndMod[1].intValue();
                        password += set.get(mod);
                    }
                }
            }

        }
        return password;
    }

    
    private String shuffleTemplate(String s){

        BigInteger bigInt = new BigInteger(hashValue);

        int index;
        char temp;
        char[] array = s.toCharArray();
        for (int i = array.length - 1; i > 0; i--)
        {
            BigInteger tempInt = BigInteger.valueOf(i);
            BigInteger[] divAndMod = bigInt.divideAndRemainder(tempInt);
            bigInt = divAndMod[0];
            index = divAndMod[1].intValue();
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return String.valueOf(array);
    }


    protected void deleteFinalize() throws Throwable {
        Clearer.zero(this.hashValue);
        super.finalize();
    }
}