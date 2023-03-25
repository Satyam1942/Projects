package com.example.garbadge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class showComplaints extends AppCompatActivity {
String authToken;
ListView lv ;
DatabaseReference dbrf;
    ArrayList<String>arrayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaints);
        authToken = getIntent().getStringExtra("authToken");
        lv = findViewById(R.id.complaintsList);
        dbrf = FirebaseDatabase.getInstance().getReference().child(authToken).child("Complaints");
        complaintLoad();



    }

    private void complaintLoad() {

        ArrayAdapter adp = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adp);
        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    arrayList.add(snapshot.getValue().toString());
                    adp.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(showComplaints.this, "No complaints", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(showComplaints.this, "No complaints found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backShowComplaints(View view)
    {

        Intent intent = new Intent(showComplaints.this,portalHome.class);
        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();

    }
}