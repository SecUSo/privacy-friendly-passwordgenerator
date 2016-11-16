package org.secuso.privacyfriendlypasswordgenerator.generator;

/**
 * Exception for 0 iteration count or no call to hash in PasswordGenerator.
 *
 * Class taken from https://github.com/pinae/ctSESAM-android/
 * last access 1st November 2016
 */
public class NotHashedException extends Exception {

    public NotHashedException(String msg) {
        super(msg);
    }

}
