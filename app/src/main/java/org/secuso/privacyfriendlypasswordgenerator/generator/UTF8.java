package org.secuso.privacyfriendlypasswordgenerator.generator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Helper class for conversions to UTF-8. Uses SecureByteArrayOutputStream.
 *
 * encode method taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 *
 */
public class UTF8 {

    public static byte[] encode(CharSequence input) {
        SecureByteArrayOutputStream stream = new SecureByteArrayOutputStream();
        if (!Charset.isSupported("UTF-8")) {
            System.out.println("UTF-8 is not supported.");
        }
        OutputStreamWriter writer = new OutputStreamWriter(stream, Charset.forName("UTF-8"));
        try {
            for (int i = 0; i < input.length(); i++) {
                writer.write(input.charAt(i));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] output = stream.toByteArray();
        stream.emptyBuffer();
        return output;
    }

    public static char[] decode(byte[] input) {
        String temp = null;

        if (!Charset.isSupported("UTF-8")) {
            System.out.println("UTF-8 is not supported.");
        }

        try {
            temp = new String(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return temp.toCharArray();
    }


}