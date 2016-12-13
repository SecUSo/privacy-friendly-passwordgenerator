package org.secuso.privacyfriendlypasswordgenerator;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.UTF8;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

/**
 * Unit tests for the UTF-8 converter.
 */
public class UTF8Test {

    @Test
    public void testEncode() {
        byte[] expected;
        try {
            expected = "testü".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            assertTrue(false);
            expected = "testü".getBytes();
        }
        byte[] converted = UTF8.encode("testü");
        assertEquals(expected.length, converted.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], converted[i]);
        }
    }

}