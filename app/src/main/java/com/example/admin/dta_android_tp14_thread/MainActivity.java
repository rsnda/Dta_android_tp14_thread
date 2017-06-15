package com.example.admin.dta_android_tp14_thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    // Création de la clé utilisé dans le bundle =
    private final String PROGRESS_BAR_ID = "PROGRESS_BAR";

    // Handler utilisé pour la communication entre le thread en background et l'UIThread
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            int p = msg.getData().getInt(PROGRESS_BAR_ID);
            progressBar.setProgress(p);
        }

    };

    // l'AtomicBoolean gère la déstruction et la mise en pause du thread
    AtomicBoolean isRunning = new AtomicBoolean(false);
    AtomicBoolean isPausing = new AtomicBoolean(false);


    // Création de l'activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Définition de la progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    // Méthode du lancement de l'activity
    @Override
    public void onStart(){
        super.onStart();

        progressBar.setProgress(0);

        // Définition du thread de background
        Thread background = new Thread(new Runnable() {

            // Le bundle porte le message et sera transmis au handler
            Bundle messageBundle = new Bundle();

            // Le message qui sera échangé entre le thread et le handler
            Message monMessage;

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100 && isRunning.get(); i++) {
                        while (isPausing.get() && (isRunning.get())) {
                            Thread.sleep(1000);
                        }
                        Thread.sleep(300);

                        /* ENVOIE DU MESSAGE AU HANDLER */
                        // Instanciation du message
                        monMessage = handler.obtainMessage();

                        // Ajout de donnée au Bundle
                        messageBundle.putInt(PROGRESS_BAR_ID, i);

                        // Ajout du Bundle au message
                        monMessage.setData(messageBundle);

                        // Envoie du message
                        handler.sendMessage(monMessage);
                    }
                }catch (Throwable t){

                }

            }
        });

        isRunning.set(true);
        isPausing.set(false);

        // Lancement du Thread
        background.start();
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
