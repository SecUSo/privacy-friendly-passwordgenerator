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

    public static String decode(byte[] input) {
        String temp = null;

        if (!Charset.isSupported("UTF-8")) {
            System.out.println("UTF-8 is not supported.");
        }

        try {
            temp = new String(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return temp;
    }


}