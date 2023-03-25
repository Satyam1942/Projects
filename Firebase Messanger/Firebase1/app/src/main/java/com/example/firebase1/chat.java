package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chat extends AppCompatActivity
{
    DatabaseReference dbrf;
    String authToken;
    ArrayList<String> contactList = new ArrayList<>();
    ListView lv ;
    DataSnapshot i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lv = findViewById(R.id.contactsListView);

        dbrf = FirebaseDatabase.getInstance().getReference().child("Users");
        authToken = getIntent().getStringExtra("authToken");
        readContacts(dbrf);

        Intent intent1 = new Intent(chat.this, chatRoom.class);
        String to;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String to = contactList.get(i);
                intent1.putExtra("from",authToken);
                intent1.putExtra("to",to);
                startActivity(intent1);
                finish();
            }
        });

    }


    private void readContacts(DatabaseReference dbrf) {

        ArrayAdapter<String> adp = new ArrayAdapter<>(chat.this, android.R.layout.simple_dropdown_item_1line , contactList);
        lv.setAdapter(adp);

       dbrf.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   for (DataSnapshot ds:snapshot.getChildren())
                   {
                   contactList.add(ds.getKey());
                   adp.notifyDataSetChanged();
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    public  void home(View view)
    {
        Intent intent = new Intent(chat.this,PortalHome.class);
        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();
    }


}

