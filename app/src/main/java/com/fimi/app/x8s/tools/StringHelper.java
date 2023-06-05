package com.fimi.app.x8s.tools;

import androidx.annotation.NonNull;

public class StringHelper {
    /**
     * Извлекает float число из текста, при условие что текст начинается с цифры
     */
    public static float parseFloat(@NonNull String inputString) {
        String floatString = "";
        boolean isNegative = false;
        boolean foundDecimalPoint = false;

        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);

            if (currentChar == '-') {
                isNegative = true;
            } else if (currentChar == '.') {
                if (!foundDecimalPoint) {
                    foundDecimalPoint = true;
                    floatString += currentChar;
                } else {
                    break;
                }
            } else if (Character.isDigit(currentChar)) {
                floatString += currentChar;
            } else {
                break;
            }
        }

        if (floatString.isEmpty()) {
            return 0;
        } else if (isNegative) {
            return Float.parseFloat("-" + floatString);
        } else {
            return Float.parseFloat(floatString);
        }
    }
}
