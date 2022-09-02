package com.example.infoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class SifremiUnuttumActivity extends AppCompatActivity {

    Button sifreOlustur,randomYenile;
    EditText random;

    public int randUretme()
    {
        Random rand = new Random();
        int Low = 100000;
        int High = 999999;
        int sifre=rand.nextInt(High-Low) + Low;

        return sifre;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);



        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sifreOlustur=findViewById(R.id.sifreOlustur);
        random=findViewById(R.id.randomSayi);
        randomYenile=findViewById(R.id.randomYenile);

        random.setText(String.valueOf(randUretme()));

        randomYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                vibe.vibrate(40);
                random.setText(String.valueOf(randUretme()));
            }
        });

    }
}