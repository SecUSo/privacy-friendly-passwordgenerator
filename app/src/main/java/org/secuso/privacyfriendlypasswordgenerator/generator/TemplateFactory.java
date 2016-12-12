package org.secuso.privacyfriendlypasswordgenerator.generator;

/**
 * Created by karo on 12.12.16.
 */

public class TemplateFactory {

    public static String createTemplateFromParameters(int specialCharacters, int lowerCaseLetters, int upperCaseLetters,
                                 int numbers, int length){

        int lengthCount = 0;
        String template = "";
        if (specialCharacters == 1) {
            template += "s";
            lengthCount ++;
        }
        if (lowerCaseLetters == 1) {
            template += "a";
            lengthCount ++;
        }
        if (upperCaseLetters == 1) {
            template += "A";
            lengthCount ++;
        }
        if (numbers == 1) {
            template += "n";
            lengthCount ++;
        }

        for (int i=lengthCount; i<length; i++){
            template += "x";
        }

        return template;
    }

}
