package org.secuso.privacyfriendlypasswordgenerator.generator;

import java.util.ArrayList;
import java.util.List;

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
            length ++;
        }
        if (lowerCaseLetters == 1) {
            template += "a";
            length ++;
        }
        if (upperCaseLetters == 1) {
            template += "A";
            length ++;
        }
        if (numbers == 1) {
            template += "n";
            length ++;
        }

        for (int i=lengthCount; i<length; i++){
            template += "x";
        }

        return template;
    }

}
