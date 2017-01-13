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

import java.io.ByteArrayOutputStream;

/**
 * This class lets you empty the buffer of an ByteArrayOutputStream.
 *
 *  Class taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 */
public class SecureByteArrayOutputStream extends ByteArrayOutputStream {
    public void emptyBuffer() {
        for (int i = 0; i < this.buf.length; i++) {
            this.buf[i] = 0x00;
        }
    }

    protected void finalize() throws Throwable {
        for (int i = 0; i < this.buf.length; i++) {
            this.buf[i] = 0x00;
        }
        super.finalize();
    }
}