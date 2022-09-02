package com.example.infoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UniversiteActivity extends AppCompatActivity {

    String TAG = "UniversiteActivity";
    String id;
    ListView liste;
    UserInformation[] userArray;
    Context context=this;

    public void fetchData(DataSnapshot dataSnapshot, String searchString, String id) {

        //db de üniversite öğrencilerinin sayısını döngüyle alıp index değişkeninde tuttuk
        int index=0;
        //eğer arama stringi boş değilse
        if( !(searchString.equals("")) ){
            //arama stringi boş değilse
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                UserInformation uInfo = ds.getValue(UserInformation.class);
                if ( (uInfo.getTip().equals("true")) && (uInfo.getUniversite().toLowerCase().contains(searchString.toLowerCase())) && !(uInfo.getUid().contains(id)) ){
                    index++;
                }
            }

            //gerekli uzunlukta user model arrayi oluşturulur
            userArray= new UserInformation[index];

            int i=0;
            //data array'e yerleştirilir
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                UserInformation uInfo = ds.getValue(UserInformation.class);
                if( (uInfo.getTip().equals("true")) && (uInfo.getUniversite().toLowerCase().contains(searchString.toLowerCase())) && !(uInfo.getUid().contains(id)) ){
                    userArray[i]=uInfo;
                    ++i;
                }
            }
            //listviewda gösterilmek istenen veriler  array adaptera verilmek üzere
            // userNames Arrayine aktarılır.
            i=0;
            final String[] userNames =new String[index];
            final String[] userIds=new String[index];
            for (UserInformation user : userArray){
                userNames[i]=user.getAdSoyad()+'\n'+user.getUniversite();
                userIds[i]=user.getUid();
                ++i;
            }

            //Elde edilen array Listview'a adapter yardımıyla gönderilir
            liste=(ListView)findViewById(R.id.liste);
            ArrayAdapter<String> userInformationArrayAdapter =new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1, userNames
            );
            liste.setAdapter(userInformationArrayAdapter);
            liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //mesajActivitye gidilecek
                    Intent intent = new Intent(UniversiteActivity.this,MesajActivity.class);
                    intent.putExtra("Name", userArray[position].getAdSoyad());
                    intent.putExtra("Id",userIds[position]);
                    startActivity(intent);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"Boş Arama Yapılamaz", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universite);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        id =mUser.getUid();

        //arama butonu ve arama stringi
        Button M_btn_search = findViewById(R.id.btn_ara);
        final EditText M_SearchString = findViewById(R.id.search_bar);
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //ara butonunun listenerı
        M_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(40);
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usersRef = rootRef.child("users");

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG,"created activity_universite");
                        fetchData(dataSnapshot, M_SearchString.getText().toString(), id);
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
}

