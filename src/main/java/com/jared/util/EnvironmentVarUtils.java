package com.jared.util;


import com.google.common.base.Strings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility for checking the presence of environment variables and PATH values.
 */
public class EnvironmentVarUtils {

    /**
     * Returns the value of an environment variable. Throws {@link IllegalArgumentException} if no environment
     * variable matching the input String exists (not case-sensitive).
     *
     * @param variableName String name of an environment variable.
     * @return String value of the environment variable.
     * @throws IllegalArgumentException if no environment variable matching the input exists (not case-sensitive).
     */
    public static String getValueOfEnvVariable(String variableName) {
        String value = System.getenv(variableName);
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException(variableName + " is not an existing environment variable: " + System.getenv());
        } else {
            return value;
        }
    }

    /**
     * Returns True if the input environment variable exists, else returns False. Not case-sensitive.
     *
     * @param variableName String name of an environment variable. Not case-sensitive.
     * @return True if a value exists for the input environment variable, else returns False.
     */
    public static boolean hasEnvVariable(String variableName) {
        try {
            getValueOfEnvVariable(variableName);
            return true;
        } catch (IllegalArgumentException e) { //thrown if specified variable name doesn't exist
            return false;
        }
    }

    /**
     * Returns the first PATH value that ends with or matches the input String. Example: PATH value is "C:\\Users\\user\\java",
     * inputting "\\user\\java" will return the full PATH value of "C:\\Users\\user\java". Input is not case-sensitive.
     *
     * @param trailingSubStr Not case-sensitive ending (or full) String of the PATH value to retrieve.
     * @return PATH value String.
     * @throws IllegalArgumentException if no PATH values ending with the specified String exist.
     */
    private static String getPathValueEndingWith(String trailingSubStr) {
        List<String> pathValues = getPathValuesAsList();

        for (String val : pathValues) {
            if (val.length() >= trailingSubStr.length()) {
                String s = val.substring(val.length() - trailingSubStr.length());
                if (s.equalsIgnoreCase(trailingSubStr)) { //Windows file paths are not case-sensitive
                    return val;
                }
            }
        }
        throw new IllegalArgumentException("Value ending with '" + trailingSubStr
                + "' not found in PATH: " + pathValues);
    }

    /**
     * Returns True if a PATH value ending with or matching the input String exists, else returns False.
     *
     * @param trailingSubStr Not Case-sensitive ending (or full) String of the PATH value to check existence of.
     * @return boolean True or False.
     */
    public static boolean hasPathValueEndingWith(String trailingSubStr) {
        try {
            getPathValueEndingWith(trailingSubStr);
            return true;
        } catch (IllegalArgumentException e) { //PATH value ending with trailing String does not exist
            return false;
        }
    }

    /**
     * Returns the values of the PATH environment variable as a {@link List} of Strings.
     *
     * @return {@link List} of PATH value Strings.
     */
    private static List<String> getPathValuesAsList() {
        String asStr = getValueOfEnvVariable("Path");
        return Utils.listFromDelimitedString(asStr, ";").stream()
                .filter(t -> !Strings.isNullOrEmpty(t)) //do not include null or empty values in returned List
                .collect(Collectors.toList());
    }
}
