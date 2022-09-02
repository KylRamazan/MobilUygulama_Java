package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class UniversiteKayitOlActivity extends AppCompatActivity {

    private String uid;
    private FirebaseAuth mAuth;
    final String TAG = "LiseKayitOlActivity";
    EditText M_mail,M_sifre,M_bolum,M_universite,M_adSoyad,M_kullaniciAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universite_kayit_ol);

        mAuth = FirebaseAuth.getInstance();

        Button M_btn_kayit = findViewById(R.id.universiteKayitOl);
        M_mail = findViewById(R.id.mail);
        M_sifre = findViewById(R.id.sifre);
        M_bolum = findViewById(R.id.bolum);
        M_universite = findViewById(R.id.universite);
        M_adSoyad = findViewById(R.id.adSoyad);
        M_kullaniciAdi = findViewById(R.id.kullaniciAdi);



        M_btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up(M_mail.getText().toString(), M_sifre.getText().toString());
            }
        });
    }

    private void sign_up(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update database  with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(UniversiteKayitOlActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mail = database.getReference("users/"+user.getUid()+"/" + "mail");
                            DatabaseReference adSoyad = database.getReference("users/"+user.getUid()+"/" + "adSoyad");
                            DatabaseReference universite = database.getReference("users/"+user.getUid()+"/" + "universite");
                            DatabaseReference bolum = database.getReference("users/"+user.getUid()+"/" + "bolum");
                            DatabaseReference tip = database.getReference("users/"+user.getUid()+"/tip");
                            DatabaseReference sifre = database.getReference("users/"+user.getUid()+"/sifre");
                            DatabaseReference kullaniciAdi = database.getReference("users/"+user.getUid()+"/kullaniciAdi");
                            DatabaseReference uid = database.getReference("users/"+user.getUid()+"/uid");

                            uid.setValue(user.getUid());
                            mail.setValue(M_mail.getText().toString());
                            adSoyad.setValue(M_adSoyad.getText().toString());
                            universite.setValue(M_universite.getText().toString());
                            bolum.setValue(M_bolum.getText().toString());
                            tip.setValue(Boolean.TRUE.toString());
                            sifre.setValue(M_sifre.getText().toString());
                            kullaniciAdi.setValue(M_kullaniciAdi.getText().toString());

                            DatabaseReference messageBox = database.getReference("messages/"+user.getUid());
                            messageBox.setValue(M_kullaniciAdi.getText().toString());

                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UniversiteKayitOlActivity.this, "Kayıt Başarısız!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}