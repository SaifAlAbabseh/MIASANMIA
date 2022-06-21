package com.example.workway;
public class CheckPassForReq {
    public String CheckPassIfMeetTheReq(String userEnteredPassword){
        if(userEnteredPassword.length()<8){
            return "Password is too short";
        }
        else if(userEnteredPassword.length()>15){
            return "Passowrd is too long";
        }
        else if(userEnteredPassword.contains(" ")){
            return "Password cannot contain spaces";
        }
        else if(!IfThereACapitalLetter(userEnteredPassword)){
            return "Password should contain atleast one capital letter";
        }
        else if(!IfThereASmallLetter(userEnteredPassword)){
            return "Password should contain atleast one small letter";
        }
        else if(!IfThereANumber(userEnteredPassword)){
            return "Password should contain atleast one number";
        }
        else if(!IfThereASymbol(userEnteredPassword)){
            return "Password should contain atleast one symbol";
        }
        return "good";
    }
    public boolean IfThereACapitalLetter(String userEnteredPassword){
        for (int i=0;i<userEnteredPassword.length();++i)
            if (((int)(userEnteredPassword.charAt(i)))>=65 && ((int)(userEnteredPassword.charAt(i)))<=90)
                return true;
        return false;
    }
    public boolean IfThereASmallLetter(String userEnteredPassword){
        for (int i=0;i<userEnteredPassword.length();++i)
            if (((int)(userEnteredPassword.charAt(i)))>=97 && ((int)(userEnteredPassword.charAt(i)))<=122)
                return true;
        return false;
    }
    public boolean IfThereANumber(String userEnteredPassword) {
        for (int i = 0; i < userEnteredPassword.length(); ++i)
            for (int j = 0; j <= 9; ++j)
                if (("" + userEnteredPassword.charAt(i)).equals("" + j))
                    return true;
        return false;
    }
    public boolean IfThereASymbol(String userEnteredPassword){
        boolean temp=false;
        for (int i=0;i<userEnteredPassword.length();++i){
            if (!(((int)(userEnteredPassword.charAt(i)))>=65 && ((int)(userEnteredPassword.charAt(i)))<=90)){
                if (!(((int)(userEnteredPassword.charAt(i)))>=97 && ((int)(userEnteredPassword.charAt(i)))<=122)){
                    if(!((""+userEnteredPassword.charAt(i)).equals(" "))){
                        for (int j = 0; j <= 9; ++j) {
                            if (!(("" + userEnteredPassword.charAt(i)).equals("" + j))) {
                                temp=true;
                            }
                            else{
                                temp=false;
                                break;
                            }
                        }
                        if(temp){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}