package com.example.infoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.lang.String;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MesajlarActivity extends AppCompatActivity {

    private String TAG = "MesajlarActivity";
    ListView liste;

    public UserInformation[] users;
    public String[] chatKeys;
    public  String[] userNames;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajlar);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id =user.getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        ValueEventListener valueUserEventListener = new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchUserData(dataSnapshot,id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        usersRef.addListenerForSingleValueEvent(valueUserEventListener);

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child("messages/"+user.getUid()+"/");
        ValueEventListener valueMessageEventListener = new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchChatData(dataSnapshot,id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        messagesRef.addListenerForSingleValueEvent(valueMessageEventListener);


    }

    public void fetchUserData(DataSnapshot dataSnapshot, String id){
        int index=0;
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if (!(ds.getKey().contains(id)) ){
                index++;
            }
        }
        int i=0;

        users= new UserInformation[index];
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo=ds.getValue(UserInformation.class);
            if (!(ds.getKey().contains(id)) ){
                users[i]=uInfo;
                System.out.println("KEY:"+ds.getKey());
                i++;
            }
        }
    }

    public void fetchChatData(DataSnapshot dataSnapshot,String id){
        int index=0;
        for(DataSnapshot ds : dataSnapshot.getChildren()){
                index++;
        }
        int i=0;
        chatKeys=new String[index];
        final String[] userNames=new String[index];
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            System.out.println("dsKey:"+ds.getKey());
            if ( users[i].getUid().equals(ds.getKey()) ){
                chatKeys[i]=ds.getKey();
                userNames[i]=users[i].getAdSoyad();
                i++;
            }
        }

        System.out.println("USERNAME:"+userNames[0]);
        //Elde edilen array Listview'a adapter yardımıyla gönderilir
        liste=(ListView)findViewById(R.id.liste);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, userNames);
        liste.setAdapter(arrayAdapter);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MesajlarActivity.this,MesajActivity.class);
                intent.putExtra("Name", userNames[position]);
                intent.putExtra("Id",chatKeys[position]);
                startActivity(intent);
            }
        });

    }


}