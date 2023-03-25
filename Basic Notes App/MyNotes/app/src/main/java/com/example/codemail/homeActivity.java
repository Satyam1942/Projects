package com.example.codemail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class homeActivity extends AppCompatActivity {
    TextView dataNotFound;
    ScrollView scl;
    ListView lv;
    ArrayList<String> notesHeader = new ArrayList<>();
    String auth;
    int curr_Auth = -1;
    ArrayAdapter<String >adp;
    int d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dataNotFound = findViewById(R.id.dataNotFound);
        scl = findViewById(R.id.scrollView2);
        for (int i = 0; i <= 100; i++) {

            SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), MODE_PRIVATE);
            if (!sharedPreferences.contains("initialized")) {
                if (i > 0)
                    break;

                dataNotFound.setVisibility(View.VISIBLE);
                scl.setVisibility(View.INVISIBLE);

                if (i == 0)
                    break;
            } else {
                curr_Auth = i;
                dataNotFound.setVisibility(View.INVISIBLE);
                scl.setVisibility(View.VISIBLE);
                lv = findViewById(R.id.listView);
                adp = new ArrayAdapter<>(homeActivity.this, android.R.layout.simple_list_item_1,notesHeader);
                lv.setAdapter(adp);
                notesHeader.add(sharedPreferences.getString("headingText", ""));
                adp.notifyDataSetChanged();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(homeActivity.this, makeNote.class);
                        auth = String.valueOf(i);
                        intent.putExtra("auth", auth);
                        startActivity(intent);
                        finish();
                    }
                });


            }

        }
    }

    public void createNote(View view) {
        Intent intent = new Intent(this, makeNote.class);
        auth = "";
        intent.putExtra("auth", auth);
        intent.putExtra("curr_auth", curr_Auth);
        startActivity(intent);
        finish();

    }

    public void deleteAllNotes(View view) {
        try {
            for (int i = 0; i <= 100; i++) {
                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(i), MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
            }
            notesHeader.clear();
            adp.notifyDataSetChanged();
            dataNotFound.setVisibility(View.VISIBLE);
            scl.setVisibility(View.INVISIBLE);
            curr_Auth = -1;
        }
        catch(Exception e)
        {
            Toast.makeText(this, "No Data to Delete", Toast.LENGTH_SHORT).show();
        }
    }

}
