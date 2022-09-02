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

public class IstatistikActivity extends AppCompatActivity {

    String TAG = "BolumActivity";
    ListView liste;
    UserInformation[] userArray;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istatistik);

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
                DatabaseReference usersRef = rootRef.child("istatistics");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //fetchData(dataSnapshot, M_SearchString.getText().toString() );
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