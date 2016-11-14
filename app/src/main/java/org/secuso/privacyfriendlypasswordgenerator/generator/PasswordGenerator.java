package org.secuso.privacyfriendlypasswordgenerator.generator;

        import java.io.UnsupportedEncodingException;
        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.ArrayList;
        import java.util.List;

public class PasswordGenerator {

    private byte[] hashValue;

    public void initialize(String domain, String masterPassword) {
        try {
            hashValue = (domain + masterPassword + "secuso ist toll").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void hash(int iterations) {
        for (int i = 0; i < iterations; i++) {
            try {
                MessageDigest hasher = MessageDigest.getInstance("SHA-256");
                hasher.update(hashValue);
                hashValue = hasher.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPassword(int specialCharacters, int letters,
                              int numbers, int length) {
        BigInteger hashNumber = BigInteger.valueOf(0);
        for (int i = 0; i < hashValue.length; i++) {
            hashNumber = hashNumber.multiply(BigInteger.valueOf(8)).
                    add(BigInteger.valueOf(hashValue[i] & 0xFF));
        }
        String password = "";
        if (specialCharacters == 1 || letters  == 1|| numbers == 1) {
            List<String> characterSet = new ArrayList<String>();
            if (specialCharacters == 1) {
                characterSet.add("#");
                characterSet.add("$");
                characterSet.add("|");
                characterSet.add(";");
                characterSet.add("&");
                characterSet.add("!");
                characterSet.add("{");
                characterSet.add("}");
                characterSet.add("(");
                characterSet.add(")");
                characterSet.add("§");
                characterSet.add("<");
                characterSet.add(">");
                characterSet.add("=");
                characterSet.add("*");
                characterSet.add("?");
                characterSet.add("-");
                characterSet.add(":");
                characterSet.add("@");
                characterSet.add("%");
                characterSet.add("+");
            }
            if (letters == 1) {
                characterSet.add("a"); characterSet.add("A");
                characterSet.add("b"); characterSet.add("B");
                characterSet.add("c"); characterSet.add("C");
                characterSet.add("d"); characterSet.add("D");
                characterSet.add("e"); characterSet.add("E");
                characterSet.add("f"); characterSet.add("F");
                characterSet.add("g"); characterSet.add("G");
                characterSet.add("h"); characterSet.add("H");
                characterSet.add("i");
                characterSet.add("j"); characterSet.add("J");
                characterSet.add("k"); characterSet.add("K");
                characterSet.add("L");
                characterSet.add("m"); characterSet.add("M");
                characterSet.add("n"); characterSet.add("N");
                characterSet.add("o");
                characterSet.add("p"); characterSet.add("P");
                characterSet.add("q"); characterSet.add("Q");
                characterSet.add("r"); characterSet.add("R");
                characterSet.add("s");
                characterSet.add("t"); characterSet.add("T");
                characterSet.add("u"); characterSet.add("U");
                characterSet.add("v"); characterSet.add("V");
                characterSet.add("w"); characterSet.add("W");
                characterSet.add("x"); characterSet.add("X");
                characterSet.add("y"); characterSet.add("Y");
                characterSet.add("z"); characterSet.add("Z");
            }
            if (numbers == 1) {
                characterSet.add("0");
                characterSet.add("1");
                characterSet.add("2");
                characterSet.add("3");
                characterSet.add("4");
                characterSet.add("5");
                characterSet.add("6");
                characterSet.add("7");
                characterSet.add("8");
                characterSet.add("9");
            }
            BigInteger setSize = BigInteger.valueOf(characterSet.size());
            while (hashNumber.compareTo(setSize) >= 0) {
                BigInteger[] divAndMod = hashNumber.divideAndRemainder(setSize);
                hashNumber = divAndMod[0].add(BigInteger.valueOf(1));
                int mod = divAndMod[1].intValue();
                password = password + characterSet.get(mod);
            }
            password = password + characterSet.get(hashNumber.intValue());
        }
        return password.substring(0, Math.min(length, password.length()));
    }

}