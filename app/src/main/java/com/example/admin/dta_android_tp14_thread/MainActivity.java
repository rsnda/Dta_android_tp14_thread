package com.example.admin.dta_android_tp14_thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    String progressId = "PROGRESS_BAR";

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            int p=msg.getData().getInt("PROGRESS_BAR");
            progressBar.setProgress(p);
        }

    };

    AtomicBoolean isRunning = new AtomicBoolean(false);
    AtomicBoolean isPausing = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onStart(){
        super.onStart();

        progressBar.setProgress(0);

        Thread thread = new Thread(new Runnable() {

            Bundle messageBundle = new Bundle();
            Message monMessage;

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100 && isRunning.get(); i++) {
                        while (isPausing.get() && (isRunning.get())) {
                            Thread.sleep(1000);
                        }
                        Thread.sleep(300);


                        monMessage = handler.obtainMessage();
                        messageBundle.putInt(progressId, i);
                        monMessage.setData(messageBundle);
                        handler.sendMessage(monMessage);
                    }
                }catch (Throwable t){

                }

            }
        });

        isRunning.set(true);
        isPausing.set(true);

        thread.start();
    }


    public void onStop() {
        super.onStop();
        isRunning.set(false);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPausing.set(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isPausing.set(false);
    }
}
