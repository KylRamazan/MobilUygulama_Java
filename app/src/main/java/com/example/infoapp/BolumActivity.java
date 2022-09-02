package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BolumActivity extends AppCompatActivity {

    String TAG = "BolumActivity";
    ListView liste;
    UserInformation[] userArray;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolum);

        //arama butonu ve arama stringi
        Button M_btn_search = findViewById(R.id.btn_ara);
        final EditText M_SearchString = findViewById(R.id.search_bar);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(40);
        //ara butonunun listenerı
        M_btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersRef = rootRef.child("users");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG,"created activity_universite");
                        fetchData(dataSnapshot, M_SearchString.getText().toString() );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                };

                usersRef.addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    public void fetchData(DataSnapshot dataSnapshot, String searchString){
        System.out.println("FetchData");
        //db de üniversite öğrencilerinin sayısını döngüyle alıp index değişkeninde tuttuk
        int index=0;
        //eğer arama stringi boş değilse
        if( !(searchString.equals("")) ){
            //arama stringi boş değilse
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                UserInformation uInfo = ds.getValue(UserInformation.class);
                //üniversite öğrencisi(true ) olan ve arama ile eşleşen girdilerin sayısı alınır
                if ( (uInfo.getTip().equals("true")) && (uInfo.getBolum().contains(searchString)) ){
                    index++;
                }
            }

            //gerekli uzunlukta user model arrayi oluşturulur
            userArray= new UserInformation[index];
            int i=0;

            //data array'e yerleştirilir
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                UserInformation uInfo = ds.getValue(UserInformation.class);
                if( (uInfo.getTip().equals("true")) && (uInfo.getBolum().contains(searchString)) ){
                    //üniversite öğrencisi(true ) olan ve arama ile eşleşen girdilerin sayısı alınır
                    userArray[i]=uInfo;
                    ++i;
                }
            }
            //listviewda gösterilmek istenen veriler  array adaptera verilmek üzere
            // userNames Arrayine aktarılır.
            i=0;
            final String[] userNames =new String[index];
            for (UserInformation user : userArray){
                userNames[i]=user.getAdSoyad()+'\n'+user.getBolum();
                ++i;
            }

            //Elde edilen array Listview'a adapter yardımıyla gönderilir
            liste=(ListView)findViewById(R.id.liste);
            ArrayAdapter<String>  userInformationArrayAdapter =new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, userNames
            );
            liste.setAdapter(userInformationArrayAdapter);
            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(),userNames[position],Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"Boş Arama Yapılamaz", Toast.LENGTH_LONG).show();
        }
    }
}