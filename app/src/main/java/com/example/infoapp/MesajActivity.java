package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MesajActivity extends AppCompatActivity {

    private String TAG = "MesajActivity";
    private String adSoyad;
    private Toolbar mToolbar;
    private ImageButton mesajGonderme;
    private EditText mesajGirdisi;
    private ScrollView mScrollView;
    private TextView mesajlariGoster;
    private String currentDate;
    private String currentTime;
    private  DatabaseReference onChange; // veri değişimi sonrası verilerin çekilmesi için kullanılancak veritabanı referansı

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);

        //bu activityi çağıran activityden buraya gönderilen parametreleri yakalıyoruz.
        Bundle inComing = getIntent().getExtras();
        String name = inComing.getString("Name"); //konuşmanın başlatıldığı kişinin adı
        String id = inComing.getString("Id");// ve id alınıyor. id, veritabanında mesajları yönetirken kullanılacak

        //burada kullanıcının kendisine dair firebase tanımlamaları yer alıyor
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();


        //kullanıcı adını almak için
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getName = database.getReference("users/" + mUser.getUid() + "/adSoyad");
        getName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adSoyad = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //onchange db referansı, şu anki kullanıcının, geldiği intentte seçtiği kullanıcıyla olan konuşmasını temsil ediyor
        onChange=database.getReference("messages/"+mUser.getUid()+"/");

        //arayüz elemanlarının tanımlamaları
        mToolbar = findViewById(R.id.mesaj_menu_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(name);

        mesajGonderme = findViewById(R.id.mesaj_gonderme_buton);
        mesajGirdisi = findViewById(R.id.mesaj_giris);
        mesajlariGoster = findViewById(R.id.mesaj_metinleri);
        mScrollView = findViewById(R.id.scroll_view);

        //mesaj gönder butonu için listener tanımı
        mesajGonderme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mUser, id);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        onChange.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //bir mesaj kullanıcının inboxına düştüğünde
                if(snapshot.exists()){
                    updateMessageView(snapshot,mesajlariGoster);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
                if(snapshot.exists()){
                    updateMessageView(snapshot, mesajlariGoster);
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MesajActivity.this, "Mesajlar Alınamadı", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMessageView(DataSnapshot snapshot, TextView messageView){
        String dateStamp, message, sender, timeStamp ;
        messageView.setText("");
        for( DataSnapshot data : snapshot.getChildren() ){
            MessageInformation mInfo= data.getValue(MessageInformation.class);
            //System.out.println("DATASNAPSHOT:"+data.getValue().toString());
            sender=mInfo.sender;
            timeStamp=mInfo.time;
            dateStamp=mInfo.date;
            //System.out.println("TİMES: "+mInfo.time+" "+mInfo.date);
            message=mInfo.message;
            messageView.append(sender+":\n"+message+"\n"+timeStamp+"  "+dateStamp+"\n\n\n");
        }
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void sendMessage(FirebaseUser mUser, String id) {
        if (TextUtils.isEmpty(mesajGirdisi.getText().toString())) {
            Toast.makeText(MesajActivity.this, "Boş mesaj Gönderilemez!", Toast.LENGTH_SHORT).show();
        } else {
            Calendar date = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd");
            currentDate =dateFormat.format(date.getTime());

            Calendar time = Calendar.getInstance();
            SimpleDateFormat timeFormat;
            timeFormat = new SimpleDateFormat("hh.mm");
            currentTime = timeFormat.format(time.getTime());

            //mesajın veritabanına gönderilmesi
            FirebaseDatabase messageBox = FirebaseDatabase.getInstance();
            String key = messageBox.getReference("messages/" + mUser.getUid() + id).push().getKey();

            DatabaseReference sender = messageBox.getReference("messages/" + mUser.getUid() + "/" + id + "/" + key + "/sender");
            DatabaseReference message = messageBox.getReference("messages/" + mUser.getUid() + "/" + id + "/" + key + "/message");
            DatabaseReference timeStamp = messageBox.getReference("messages/" + mUser.getUid() + "/" + id + "/" + key + "/time");
            DatabaseReference dateStamp = messageBox.getReference("messages/" + mUser.getUid() + "/" + id + "/" + key + "/date");
            DatabaseReference mId = messageBox.getReference("messages/" + mUser.getUid() + "/" + id + "/" + key + "/id");

            sender.setValue(adSoyad);
            message.setValue(mesajGirdisi.getText().toString());
            timeStamp.setValue(currentTime);
            dateStamp.setValue(currentDate);
            mId.setValue(key);

            equalMessageBox(mUser,id);
            mesajGirdisi.setText("");
        }
    }

    public void equalMessageBox(FirebaseUser yUser,String id){

        FirebaseDatabase messageBox = FirebaseDatabase.getInstance();
        String key = messageBox.getReference("messages/" + id + yUser.getUid()).push().getKey();

        DatabaseReference sender = messageBox.getReference("messages/" + id+ "/" + yUser.getUid() + "/" + key + "/sender");
        DatabaseReference message = messageBox.getReference("messages/" +  id+ "/" + yUser.getUid() + "/" + key + "/message");
        DatabaseReference timeStamp = messageBox.getReference("messages/" + id + "/" + yUser.getUid() + "/" + key + "/time");
        DatabaseReference dateStamp = messageBox.getReference("messages/" + id + "/" +yUser.getUid()+ "/" + key + "/date");
        DatabaseReference mId=messageBox.getReference("messages/" + id + "/" +yUser.getUid()+ "/" + key + "/id");

        sender.setValue(adSoyad);
        message.setValue(mesajGirdisi.getText().toString());
        timeStamp.setValue(currentTime);
        dateStamp.setValue(currentDate);
        mId.setValue(key);
    }
}