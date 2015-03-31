package com.example.damien.pi_set;

import android.os.Handler;

/**
 * Created by Boris on 23/03/2015.
 */
public class Timer extends Thread {

    long startTime = 0;
    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {


    @Override
    public void run() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        MainActivity.timerTextView.setText(String.format("%d:%02d", minutes, seconds));

        MainActivity.handler.postDelayed(this, 500);
    }
};

    public void run() {

        startTime = System.currentTimeMillis();

        while (true){
            try {
                this.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timerHandler.post(timerRunnable);
        }
    }
       }


