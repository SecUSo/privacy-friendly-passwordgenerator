package org.secuso.privacyfriendlypasswordgenerator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;

/**
 * Tests the PasswordGenerator
 */

public class PasswordGeneratorTest {

    @Test
    public void testGetPassword() {

        PasswordGenerator generator = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512",
                "10");

        String first = generator.getPassword(1, 1, 1, 1, 12);

        PasswordGenerator generatorSecond = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512",
                "10");

        String second = generatorSecond.getPassword(1, 1, 1, 1, 12);

        assertEquals("Passwords do not match", first, second);

    }

    @Test
    public void testGetPasswordWithSpecifiedValues() {
        PasswordGenerator generator = getDefaultGenerator();
        for (int i = 0; i < 2; i++) {
            //Test character sets
            assertEquals("9342", generator.getPassword(0, 0, 0, 1, 4));
            assertEquals("XWDL", generator.getPassword(0, 0, 1, 0, 4));
            assertEquals("1D4S", generator.getPassword(0, 0, 1, 1, 4));
            assertEquals("xwdl", generator.getPassword(0, 1, 0, 0, 4));
            assertEquals("1d4s", generator.getPassword(0, 1, 0, 1, 4));
            assertEquals("9zHg", generator.getPassword(0, 1, 1, 1, 4));
            assertEquals("]:\"-", generator.getPassword(1, 0, 0, 0, 4));
            assertEquals("/&2<", generator.getPassword(1, 0, 0, 1, 4));
            assertEquals(".\"Y_", generator.getPassword(1, 0, 1, 0, 4));
            assertEquals("9-P{", generator.getPassword(1, 0, 1, 1, 4));
            assertEquals(".\"y_", generator.getPassword(1, 1, 0, 0, 4));
            assertEquals("9-p{", generator.getPassword(1, 1, 0, 1, 4));
            assertEquals("X6z[", generator.getPassword(1, 1, 1, 1, 4));
        }
        //Test password length
        String[] lengthTestExpected = new String[]{"X6z[", "X6z&9", "%Z@k9E", "%X.G#z3", "%]9ISnVz", "%X]C#<8zF", "%XnJ3&8F[&", "%X]c[RQC1&P", "%X]^eRe&4~UM", "%]9gO63FCJjX1", "%]f>0.UbRePM{1", "X6t°0.Ubl~:VmDv"};
        for (int i = 4; i < 16; i++) {
            assertEquals(lengthTestExpected[i - 4], generator.getPassword(1, 1, 1, 1, i));
        }
    }

    @Test
    public void testPasswordGeneratorWithSpecifiedValues() {
        String[] generatedPasswords = new String[3 * 10 * 2];
        for (int cost = 10; cost < 13; cost++) {
            for (int iterations = 1000; iterations <= 10000; iterations += 1000) {
                for (int iteration = 1; iteration < 3; iteration++) {
                    PasswordGenerator generator = new PasswordGenerator(
                            "test.com", "hugo", "masterpassword", "SECUSO", iteration, iterations, "SHA256", Integer.toString(cost)
                    );
                    int index = ((cost - 10) * 20) + (((iterations - 1000) / 1000) * 2) + (iteration - 1);
                    generatedPasswords[index] = generator.getPassword(0, 1, 1, 1, 8);
                }
            }
        }
        //check all values unique
        for (int i = 0; i < generatedPasswords.length; i++) {
            for (int j = i + 1; j < generatedPasswords.length; j++) {
                assertNotEquals(generatedPasswords[i], generatedPasswords[j]);
            }
        }
        //Compare with specified values
        String[] expectedPasswords = new String[]{
                "JcM2mNqv", "lYyVTfd2", "lwcOrH3Q", "Lxdwe63B", "p2W6kWyN", "3icHiQGV", "9gNuDfMf", "filkU4MU", "pS5sYXM4", "VeDjD9Hd", "0bLkv1ZR", "ZklM9c78",
                "hVt4A1A2", "dxcK7W27", "VCUS6cjf", "OuajLNw6", "VhUsWNV3", "9bAQDhB9", "TeI18EZI", "LFO9Fvg4", "J9lUeU8U", "Tz05be89", "P6IN8Qek", "LdiR1L0F",
                "1OWWy6iR", "Njy2huS2", "z3Pn3WZJ", "9jjnvQ0Q", "7bg9J6L0", "M65YuAlP", "W9oYw77r", "x3gVVPPd", "Jq2FxO5u", "11S5KpRJ", "q5jUBvHO", "DV7drd8A",
                "ZfqH1YUv", "9VlgNNWY", "JvH3HgkT", "hdI9IkTq", "kwVm89q0", "sEEfT3ZY", "HD7yRaH4", "xcbQ1OMv", "9IYY9rQN", "rrXPm8RZ", "9say7GDC", "VpwDWO5N",
                "9eW4MJhk", "nxq0PmB9", "Lq5fcNL5", "x0xY8Ed8", "DyrZ5hOe", "rYrjfRz3", "fRQyd6G1", "ZJ7wo7Gm", "sEod7DgG", "Rug68Vqw", "OjCG5ZZH", "OTbEV06u"};
        assertArrayEquals(expectedPasswords, generatedPasswords);
    }

    private PasswordGenerator getDefaultGenerator() {
        return new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "SECUSO",
                1,
                2000,
                "SHA256",
                "10");
    }

    @Test
    public void testUpdatedIterationPassword() {

        PasswordGenerator generator = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512",
                "10");

        String first = generator.getPassword(1, 1, 1, 1, 12);

        PasswordGenerator generatorSecond = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                3,
                4000,
                "SHA512",
                "10");

        String second = generatorSecond.getPassword(1, 1, 1, 1, 12);

        assertNotEquals("Passwords match", first, second);

    }

    @Test
    public void testShuffleString() {
        PasswordGenerator generator = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512",
                "10");
        String toShuffle = "Aaonxxxxxxxxxxxxxxxxxxxxxx";
        //String shuffled = generator.shuffleTemplate(toShuffle);

        //assertNotEquals("String not shuffled", toShuffle, shuffled);

    }

}
