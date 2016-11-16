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