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

/**
 * Base64 Encoding according BCrypts scheme
 * Methods taken from BCrypt class
 */

public class Base64 {

    // Table for Base64 encoding
    static private final char base64_code[] = {
            '.', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9'
    };

    // Table for Base64 decoding
    static private final byte index_64[] = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 0, 1, 54, 55,
            56, 57, 58, 59, 60, 61, 62, 63, -1, -1,
            -1, -1, -1, -1, -1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            -1, -1, -1, -1, -1, -1, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, -1, -1, -1, -1, -1
    };

    /**
     * Encode a byte array using bcrypt's slightly-modified base64
     * encoding scheme. Note that this is *not* compatible with
     * the standard MIME-base64 encoding.
     *
     * @param d	the byte array to encode
     * @param len	the number of bytes to encode
     * @return	base64-encoded string
     * @exception IllegalArgumentException if the length is invalid
     */
    public static String encode_base64(byte d[], int len)
            throws IllegalArgumentException {
        int off = 0;
        StringBuffer rs = new StringBuffer();
        int c1, c2;

        if (len <= 0 || len > d.length)
            throw new IllegalArgumentException ("Invalid len");

        while (off < len) {
            c1 = d[off++] & 0xff;
            rs.append(base64_code[(c1 >> 2) & 0x3f]);
            c1 = (c1 & 0x03) << 4;
            if (off >= len) {
                rs.append(base64_code[c1 & 0x3f]);
                break;
            }
            c2 = d[off++] & 0xff;
            c1 |= (c2 >> 4) & 0x0f;
            rs.append(base64_code[c1 & 0x3f]);
            c1 = (c2 & 0x0f) << 2;
            if (off >= len) {
                rs.append(base64_code[c1 & 0x3f]);
                break;
            }
            c2 = d[off++] & 0xff;
            c1 |= (c2 >> 6) & 0x03;
            rs.append(base64_code[c1 & 0x3f]);
            rs.append(base64_code[c2 & 0x3f]);
        }
        return rs.toString();
    }

    /**
     * Look up the 3 bits base64-encoded by the specified character,
     * range-checking againt conversion table
     * @param x	the base64-encoded value
     * @return	the decoded value of x
     */
    public static byte char64(char x) {
        if ((int)x < 0 || (int)x > index_64.length)
            return -1;
        return index_64[(int)x];
    }

    /**
     * Decode a string encoded using bcrypt's base64 scheme to a
     * byte array. Note that this is *not* compatible with
     * the standard MIME-base64 encoding.
     * @param s	the string to decode
     * @param maxolen	the maximum number of bytes to decode
     * @return	an array containing the decoded bytes
     * @throws IllegalArgumentException if maxolen is invalid
     */
    public static byte[] decode_base64(String s, int maxolen)
            throws IllegalArgumentException {
        StringBuffer rs = new StringBuffer();
        int off = 0, slen = s.length(), olen = 0;
        byte ret[];
        byte c1, c2, c3, c4, o;

        if (maxolen <= 0)
            throw new IllegalArgumentException ("Invalid maxolen");

        while (off < slen - 1 && olen < maxolen) {
            c1 = char64(s.charAt(off++));
            c2 = char64(s.charAt(off++));
            if (c1 == -1 || c2 == -1)
                break;
            o = (byte)(c1 << 2);
            o |= (c2 & 0x30) >> 4;
            rs.append((char)o);
            if (++olen >= maxolen || off >= slen)
                break;
            c3 = char64(s.charAt(off++));
            if (c3 == -1)
                break;
            o = (byte)((c2 & 0x0f) << 4);
            o |= (c3 & 0x3c) >> 2;
            rs.append((char)o);
            if (++olen >= maxolen || off >= slen)
                break;
            c4 = char64(s.charAt(off++));
            o = (byte)((c3 & 0x03) << 6);
            o |= c4;
            rs.append((char)o);
            ++olen;
        }

        ret = new byte[olen];
        for (off = 0; off < olen; off++)
            ret[off] = (byte)rs.charAt(off);
        return ret;
    }


}
