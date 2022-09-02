package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    Button sifre,kayitOl,giris;
    TextInputEditText kulAdi,password;
    private FirebaseAuth mAuth;
    final String TAG = "GirisActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_giris);

        mAuth = FirebaseAuth.getInstance();

        sifre=findViewById(R.id.sifremiUnuttum);
        giris=findViewById(R.id.btn_girisYap);
        kayitOl=findViewById(R.id.btn_kayitOl);
        kulAdi = findViewById(R.id.line_mail);
        password = findViewById(R.id.line_sifre);


        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                Intent intent=new Intent(GirisActivity.this,SifremiUnuttumActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                login(kulAdi.getText().toString(), password.getText().toString());
            }
        });

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viev) {
                Intent intent=new Intent(GirisActivity.this,KayitOlActivity.class);
                vibe.vibrate(40);
                startActivity(intent);
            }
        });
    }

    private void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(GirisActivity.this, "Giriş Başarılı",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();


                            //  Yukarıda olan buraya geldi
                            final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            Intent intent=new Intent(GirisActivity.this,AnasayfaActivity.class);
                            vibe.vibrate(40);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(GirisActivity.this, "Giriş Başarısız!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}