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
 *
 * Basic class structure and idea taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 * Added the BCrypt component
 *
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
                        UTF8.encode(String.valueOf(iteration*100) + domain + username + deviceID),
                        hashIterations),
                22);

        //Log.d("SEED", temp);
        //Log.d("SEED_SHORT", temp.substring(0,21));
        this.hashValue = transformPassword(BCrypt.hashpw(masterpassword, "$2a$10$" + temp));
    }

    //cuts the salt from the password
    public byte[] transformPassword (String password) {
        byte[] passwordChar = UTF8.encode(password);
        byte[] transformedPassword = new byte[31];

        //Log.d("GENERATOR", "Password after bcrypt " + password);

        for (int i=29; i<passwordChar.length; i++) {
            transformedPassword[i-29] = passwordChar[i];
        }

        //Log.d("GENERATOR", "Password after bcrypt short " + new String(transformedPassword));

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

        List<String> characterSet = new ArrayList<>();

        if (specialCharacters == 1) {
            String characters = "#!\"~|@^°$%&/()[]{}=-_+*<>;:.";
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (lowerCaseLetters == 1) {
            String characters = "abcdefghijklmnopqrstuvwxyz";
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (upperCaseLetters == 1) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }

        }

        if (numbers == 1) {
            String characters = "0123456789";
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (characterSet.size() > 0) {

            for (int i = 0; i < length; i++) {
                BigInteger setSize = BigInteger.valueOf(characterSet.size());
                BigInteger[] divAndMod = hashNumber.divideAndRemainder(setSize);
                hashNumber = divAndMod[0];
                int mod = divAndMod[1].intValue();
                password += characterSet.get(mod);
            }

        }
        return password;
    }

    public void mixTemplate() {

    }

    protected void deleteFinalize() throws Throwable {
        Clearer.zero(this.hashValue);
        super.finalize();
    }
}