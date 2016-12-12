package org.secuso.privacyfriendlypasswordgenerator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.secuso.privacyfriendlypasswordgenerator.generator.Clearer;

/**
 * Tests the clearer
 */
public class ClearerTest {

    @Test
    public void testZero() {
        byte[] a = new byte[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = 0x37;
        }
        Clearer.zero(a);
        for (int i = 0; i < a.length; i++) {
            assertEquals("Clearer not zero", 0x00, a[i]);
        }
    }

}
