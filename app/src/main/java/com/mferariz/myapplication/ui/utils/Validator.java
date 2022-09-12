package com.mferariz.myapplication.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]+$";
    public static final String REGEX_PASSWORD = "^([\\dA-Za-z]){6,12}$";

    public static boolean validate(String regex, String input){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.find();
        if(matchFound) {
            return true;
        } else {
            return false;
        }
    }

}
