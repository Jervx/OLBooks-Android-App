package com.itel316.olbooks.helpers;

public class Validator {

    public static boolean validateEmail(String toCheck) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(toCheck);
        return m.matches();
    }

    public static boolean validateNotEmpty(String toCheck) {
        return toCheck.length() > 0;
    }

    public static boolean validateIsNumber(String toCheck) {
        try {
            Integer.parseInt(toCheck);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static String multiValidate(String toCheck, String[] validation) {
        boolean result = true;
        String errs = "";
        for (String mode : validation) {
            if (mode == "validateIsEmail") {
                if (!validateEmail(toCheck)) {
                    errs = "Invalid email";
                    result = false;
                    break;
                }
            } else if (mode == "validateIsNotEmpty") {
                if (!validateNotEmpty(toCheck)) {
                    errs = "Input is empty";
                    result = false;
                    break;
                }
            } else if (mode == "validateIsNumber") {
                if (!validateIsNumber(toCheck)) {
                    errs = "Input must be a number";
                    result = false;
                    break;
                }
            }
        }
        return result ? "valid" : errs;
    }
}