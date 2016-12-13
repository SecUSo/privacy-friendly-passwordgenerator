package org.secuso.privacyfriendlypasswordgenerator.generator;

/**
 * Clears arrays.
 */
public class Clearer {

    public static void zero(byte[] a) {
        if (a != null) {
            for (int i = 0; i < a.length; i++) {
                a[i] = 0x00;
            }
        }
    }

}