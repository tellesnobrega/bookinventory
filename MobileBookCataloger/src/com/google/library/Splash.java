package com.google.library;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;

public class Splash extends Activity {
        
        private CountDownTimer countdown;
        private final long TEMPO_LIMITE_PARA_RESPONDER = 4000; //em milisegundos
        private int tick = 0;
        
        private LinearLayout mainLayout;
        private MediaPlayer player;
        
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial);
        
        
        mainLayout = (LinearLayout) findViewById(R.id.layoutInicial);
        
        countdown = new CountDownTimer(TEMPO_LIMITE_PARA_RESPONDER, 500) {
                        
                        @Override
                        public void onTick(long tempoRestante) {
                                if (tick == 2) {
                                        mainLayout.setBackgroundResource(R.drawable.telas);
                                }
                                if (tick == 3) {
                                        mainLayout.setBackgroundResource(R.drawable.telas2);
                                }
                                if (tick == 4) {
                                        mainLayout.setBackgroundResource(R.drawable.telas3);
                                }
                                if (tick == 5) {
                                        mainLayout.setBackgroundResource(R.drawable.telas4);
                                }
                                tick++;
                        }
                        
                        @Override
                        public void onFinish() {
                                Intent i = new Intent(Splash.this, Menu.class);
                        startActivity(i);
                        finish();
                }
                };
                
                countdown.start();
    }

        
        @Override
        protected void onStop() {
                super.onStop();
                countdown.cancel();
                finish();
        }
    
    
    
    
}