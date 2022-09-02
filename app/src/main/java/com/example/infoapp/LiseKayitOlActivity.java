package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LiseKayitOlActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final String TAG = "LiseKayitOlActivity";
    EditText M_mail,M_sifre,M_adSoyad,M_kullaniciAdi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lise_kayit_ol);

        mAuth = FirebaseAuth.getInstance();

        Button M_btn_kayit = findViewById(R.id.liseKayitOl);

        M_mail = findViewById(R.id.mail);
        M_adSoyad = findViewById(R.id.adSoyad);
        M_sifre = findViewById(R.id.sifre);
        M_kullaniciAdi = findViewById(R.id.kullaniciAdi);


        M_btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Selam");
                sign_up(M_mail.getText().toString(), M_sifre.getText().toString());
                System.out.println("Selam");
            }
        });
    }

    private void sign_up(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LiseKayitOlActivity.this, "Kayıt Başarılı" + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mail = database.getReference("users/"+user.getUid()+"/" + "mail");
                            DatabaseReference adSoyad = database.getReference("users/"+user.getUid()+"/" + "adSoyad");
                            DatabaseReference tip = database.getReference("users/"+user.getUid()+"/tip");
                            DatabaseReference sifre = database.getReference("users/"+user.getUid()+"/sifre");
                            DatabaseReference kullaniciAdi= database.getReference("users/"+user.getUid()+"/kullaniciAdi");

                            mail.setValue(M_mail.getText().toString());
                            adSoyad.setValue(M_adSoyad.getText().toString());
                            tip.setValue(Boolean.FALSE.toString());
                            sifre.setValue(M_sifre.getText().toString());
                            kullaniciAdi.setValue(M_kullaniciAdi.getText().toString());
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LiseKayitOlActivity.this, "Kayıt Başarısız!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}