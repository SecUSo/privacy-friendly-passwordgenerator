/**
 * This file is part of Privacy Friendly Password Generator.
 * <p>
 * Privacy Friendly Password Generator is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * <p>
 * Privacy Friendly Password Generator is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.generator;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class creates PBKDF2 with different SHA versions.
 * <p>
 * Class taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 * Modification: IllegalArgumentException has been added
 */

public class PBKDF2 {

    private static byte[] shaHMAC(String hashFunction, byte[] key, byte[] password) throws IllegalArgumentException {
        if (key.length == 0) {
            throw new IllegalArgumentException("Empty key entered.");
        }
        try {
            Mac sha_HMAC = Mac.getInstance("Hmac" + hashFunction);
            sha_HMAC.init(new SecretKeySpec(key, "Hmac" + hashFunction));
            return sha_HMAC.doFinal(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return password;
        }
    }

    private static byte[] F(String hashFunction, byte[] password, byte[] salt, int iterations, int i) {
        byte[] Si = new byte[salt.length + 4];
        System.arraycopy(salt, 0, Si, 0, salt.length);
        byte[] iByteArray = ByteBuffer.allocate(4).putInt(i).array();
        System.arraycopy(iByteArray, 0, Si, salt.length, iByteArray.length);
        byte[] U = shaHMAC(hashFunction, password, Si);
        byte[] T = new byte[U.length];
        System.arraycopy(U, 0, T, 0, T.length);
        for (int c = 1; c < iterations; c++) {
            U = shaHMAC(hashFunction, password, U);
            for (int k = 0; k < U.length; k++) {
                T[k] = (byte) (((int) T[k]) ^ ((int) U[k]));
            }
        }
        return T;
    }

    /**
     * Pass "SHA256" or "SHA384" or "SHA512" as the parameter hashFunction.
     */
    public static byte[] hmac(String hashFunction, byte[] hashString, byte[] salt, int iterations) {
        int dkLen = 64;
        int hLen = 64;
        switch (hashFunction) {
            case "SHA256":
                dkLen = 32;
                hLen = 32;
                break;
            case "SHA384":
                dkLen = 48;
                hLen = 48;
                break;
            case "SHA512":
                dkLen = 64;
                hLen = 64;
                break;
        }
        int l = (int) Math.ceil(dkLen / hLen);
        int r = dkLen - (l - 1) * hLen;
        byte[] dk = new byte[dkLen];
        for (int i = 1; i <= l; i++) {
            byte[] T = F(hashFunction, hashString, salt, iterations, i);
            for (int k = 0; k < T.length; k++) {
                if (i - 1 + k < dk.length) {
                    dk[i - 1 + k] = T[k];
                }
            }
        }
        return dk;
    }

}
