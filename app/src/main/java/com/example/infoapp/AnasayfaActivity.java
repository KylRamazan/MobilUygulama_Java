package com.example.infoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.infoapp.R;
import com.example.infoapp.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnasayfaActivity extends AppCompatActivity {
    String TAG = "AnasayfaActivity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView adSoyad;
    private TextView kullaniciAdi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference r_adSoyad = database.getReference("users/"+user.getUid()+"/adSoyad");
        adSoyad = findViewById(R.id.fullName);

        r_adSoyad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.i(TAG, "r_adSoyad başarılı");
                String s_adSoyad = dataSnapshot.getValue(String.class);
                adSoyad.setText(s_adSoyad);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // databaseden kullanıcı adının çekilmesi
        final DatabaseReference r_kullaniciAdi = database.getReference("users/"+user.getUid()+"/kullaniciAdi");
        kullaniciAdi =findViewById(R.id.anaKullaniciAdi);

        r_kullaniciAdi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s_kullaniciAdi = dataSnapshot.getValue(String.class);
                Log.i(TAG, "r_kullaniciAdi başarılı");
                kullaniciAdi.setText(s_kullaniciAdi); // anasayfa_activitydeki textin setlenmesi
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final CardView university=(CardView) findViewById(R.id.university);
        final CardView department=(CardView) findViewById(R.id.department);
        final CardView friends=(CardView) findViewById(R.id.friends);
        final CardView messages=(CardView) findViewById(R.id.messages);
        final CardView statistics=(CardView) findViewById(R.id.statistics);
        final CardView settings=(CardView) findViewById(R.id.settings);

        //telefonun dokunma geribildirimi
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this, UniversiteActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this,BolumActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this, ArkadasActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this,MesajlarActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this,IstatistikActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnasayfaActivity.this,AyarlarActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });
    }
}