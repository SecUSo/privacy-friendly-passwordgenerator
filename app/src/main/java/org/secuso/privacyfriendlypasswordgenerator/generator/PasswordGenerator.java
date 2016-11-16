package org.secuso.privacyfriendlypasswordgenerator.generator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the hashing and the creation of passwords. Please initialize first.
 * Do not forget to hash at least once because otherwise the password might look not very
 * random. It is safe to hash often because an attacker has to hash as often as you did for
 * every try of a brute-force attack. getPassword creates a password string out of the hash
 * digest.
 * <p>
 * Class structure and idea taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 */
public class PasswordGenerator {

    private byte[] hashValue;
    private final String defaultCharacterSetDigits = "0123456789";
    private final String defaultCharacterSetLowerCase = "abcdefghijklmnopqrstuvwxyz";
    private final String defaultCharacterSetUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String defaultCharacterSetExtra = "#!\"~|@^°$%&/()[]{}=-_+*<>;:.";

    public PasswordGenerator(String domain,
                             String username,
                             String masterpassword,
                             String deviceID,
                             byte[] salt,
                             int iterations) {
        
        byte[] startValue = UTF8.encode(domain + username + masterpassword + deviceID);

        this.hashValue = PBKDF2.hmac("SHA512", startValue, salt, iterations);
        Clearer.zero(startValue);
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
            String characters = this.defaultCharacterSetExtra;
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (lowerCaseLetters == 1) {
            String characters = this.defaultCharacterSetLowerCase;
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }
        }

        if (upperCaseLetters == 1) {
            String characters = this.defaultCharacterSetUpperCase;
            for (int i = 0; i < characters.length(); i++) {
                characterSet.add(Character.toString(characters.charAt(i)));
            }

        }

        if (numbers == 1) {
            String characters = this.defaultCharacterSetDigits;
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


    protected void finalize() throws Throwable {
        Clearer.zero(this.hashValue);
        super.finalize();
    }
}