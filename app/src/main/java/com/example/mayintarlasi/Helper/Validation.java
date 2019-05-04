package com.example.mayintarlasi.Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Validation {
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPass(String pass) {
        String PASS_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,16}$";
        Pattern pattern = Pattern.compile(PASS_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    public boolean isPassEmpty(String pass) {
        if (pass.length()==0) {
            return true;
        }
        else
        {
            return false;
        }
    }


    public boolean isValidFullName(String fullName) {
        if (fullName != null && fullName.length() >= 6  && fullName.length()<=30) {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean isAlpha(String name) {
        return name.matches("^\\s*[a-zA-Z,ç,Ç,ğ,Ğ,ı,İ,ö,Ö,ş,Ş,ü,Ü,\\s]+\\s*$");
    }

    public boolean isUserNameCorrect(String username)
    {if(username.length()>=5) {return true;} else {return false;}}

    public boolean isAgeDigit(String Digit) {
        try
        {
            Integer.parseInt( Digit );
            return true;
        }
        catch( Exception e )
        {
            return false;
        }
    }
    public boolean isTcCorrect(String Tc) {

        if (Tc != null && Tc.length() == 11) {
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean MaxMinAge(String Age) {
        if(Integer.valueOf(Age)>=0 && Integer.valueOf(Age)<=99 && Age!=null && Age!="") {return true;} else {return false;}}

    public boolean MaxMinName(String Name)
    {
        if (Name != null && Name.length() >= 2  && Name.length()<=15) {
            return true;
        }
        else
        {
            return false;
        }
    }
}
