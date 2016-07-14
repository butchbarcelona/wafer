package com.proj.demo;

import android.os.Handler;
import android.util.Log;

import com.proj.demo.wafer.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mbarcelona on 7/4/16.
 */
public class CountDownTimerThread {

    public interface CountDownListener{
        public void onTick(int currentCountdown);
        public void onFinish();
    }

    int startCountdown = 5;
    int currentCountdown;

    Handler countdownHandler = new Handler();
    Timer countdownTimer = new Timer();

    CountDownListener listener;
    int count = 0;
    public CountDownTimerThread(CountDownListener listener, int time) {
        this.listener = listener;
        startCountdown = time;
    }

    CountTimerTask task;
    public void startCountdownTimer() {

        Log.d(MainActivity.APP_CODE,"countDownTimer: startCountdownTimer");
        currentCountdown = startCountdown;
        //for (int i = 0; i <= startCountdown && isRunning; i++) {

        /*task = new TimerTask() {
            public void setRunning(boolean running) {
                isRunning = running;
            }

            public boolean isRunning = true;
            @Override
            public void run() {
                countdownHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(isRunning) {
                            listener.onTick(currentCountdown);
                            currentCountdown--;
                            count++;
                        }else{
                            listener.onFinish();
                        }
                    }
                });
            }
        };*/

        task = new CountTimerTask();
        task.setRunning(true);

        countdownTimer.schedule(task, 1000, 1000);

    }

    public void stopCount(){
        Log.d(MainActivity.APP_CODE,"countDownTimer: stopCount");

        if(task != null && countdownTimer != null) {
            task.setRunning(false);
            countdownTimer.cancel();
            countdownTimer.purge();
            countdownTimer = null;
            task.cancel();
            task = null;
        }
    }

    public class CountTimerTask extends TimerTask{

        public void setRunning(boolean running) {
            isRunning = running;
        }

        boolean isRunning = false;
        @Override
        public void run() {
            countdownHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(isRunning) {
                        listener.onTick(currentCountdown);
                        currentCountdown--;
                        count++;
                    }else{
                        listener.onFinish();
                    }
                }
            });
        }
    }


}
