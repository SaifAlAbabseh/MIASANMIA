package com.example.workway;

import android.os.CountDownTimer;

public class TimerForTries {
    static boolean AlreadyBlocked=false;
    public static void setTimer(){
        if(!AlreadyBlocked){
            new CountDownTimer(300000, 1000) {
                public void onTick(long millisUntilFinished) {Login.HowLong=((millisUntilFinished/1000.0)/60.0);}
                public void onFinish() {
                    TimerFinished();
                }
            }.start();
        }
    }
    public static void TimerFinished(){
        Login.Tries=0;
        Login.HowLong=5.0;
        AlreadyBlocked=false;
    }
}
