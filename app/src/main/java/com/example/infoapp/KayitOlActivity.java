package com.example.infoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class KayitOlActivity extends AppCompatActivity {

    Button lise,universite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        lise=findViewById(R.id.liseOgrenciMezun);
        universite=findViewById(R.id.universiteOgrenciMezun);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        lise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                Intent intent=new Intent(KayitOlActivity.this,LiseKayitOlActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        universite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                Intent intent=new Intent(KayitOlActivity.this,UniversiteKayitOlActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

    }
}