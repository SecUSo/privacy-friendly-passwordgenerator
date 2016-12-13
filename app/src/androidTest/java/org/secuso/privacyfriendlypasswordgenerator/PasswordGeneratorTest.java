package org.secuso.privacyfriendlypasswordgenerator;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.PasswordGenerator;

import static org.junit.Assert.*;

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
                "SHA512");

        String first = generator.getPassword(1, 1, 1, 1, 12);

        PasswordGenerator generatorSecond = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512");

        String second = generatorSecond.getPassword(1, 1, 1, 1, 12);

        assertEquals("Passwords do not match", first, second);

    }

    @Test
    public void testUpdatedIterationPassword() {

        PasswordGenerator generator = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                2,
                4000,
                "SHA512");

        String first = generator.getPassword(1, 1, 1, 1, 12);

        PasswordGenerator generatorSecond = new PasswordGenerator("test.com",
                "hugo",
                "masterpassword",
                "deviceID",
                3,
                4000,
                "SHA512");

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
                "SHA512");
        String toShuffle = "Aaonxxxxxxxxxxxxxxxxxxxxxx";
        //String shuffled = generator.shuffleTemplate(toShuffle);

        //assertNotEquals("String not shuffled", toShuffle, shuffled);

    }

}
