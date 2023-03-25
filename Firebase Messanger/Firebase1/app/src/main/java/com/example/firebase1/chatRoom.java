package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.GAuthToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class chatRoom extends AppCompatActivity {
String from,to;
TextView toText;
int msgCountFrom;
int msgCountTo;
DatabaseReference dbrf;
ArrayList<String> stringArrayList = new ArrayList<>();
ListView lv;
boolean notify =true;
String notifictaion ="";
    ArrayAdapter<String> adp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        from  = getIntent().getStringExtra("from");
        to  = getIntent().getStringExtra("to");
        toText = findViewById(R.id.toChat);
        toText.setText("Chatting With: " + to);
        lv= findViewById(R.id.messageText);
        adp = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stringArrayList);
        lv.setAdapter(adp);


        dbrf = FirebaseDatabase.getInstance().getReference().child("chatRoom");
        //retrieving data



        dbrf.child(from).child("messageCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    msgCountFrom = Integer.parseInt(snapshot.getValue().toString());


                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbrf.child(to).child("messageCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    msgCountTo = Integer.parseInt(snapshot.getValue().toString());
                    getMessage(msgCountTo);
                    Log.d("lpg",String.valueOf(msgCountTo));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMessage( int mgTo) {
            dbrf.child(to).child(String.valueOf(mgTo-1)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        stringArrayList.add(snapshot.getValue().toString());
                        adp.notifyDataSetChanged();
                        notify =true;
                        notifictaion = snapshot.getValue().toString();
                    }
                    else notify =false;
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    public void sendMessage(View view)
    {

        EditText msg = (EditText)  findViewById(R.id.message);
        String message = from+" :  "+ msg.getText().toString();
        msg.setText("");
        dbrf.child(from).child(String.valueOf(msgCountFrom)).setValue(message);
        msgCountFrom++;
        dbrf.child(from).child("messageCount").setValue(msgCountFrom);

        stringArrayList.add(message);

        adp.notifyDataSetChanged();

    }
    @Override
    public void onPause()
    {

        super.onPause();
        String CONSTANT = "message recieved";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this , CONSTANT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel =null;
        if(notify)
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel("Message Recieved",CONSTANT,NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.enableVibration(true);
                    notificationChannel.enableLights(true);
                    notificationChannel.setVibrationPattern(new  long[] {0,500,500,0});
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setDescription("Sample Description");
                    notificationManager.createNotificationChannel(notificationChannel);


            }
            builder.setAutoCancel(true).setContentTitle("Message From : "+to).setContentText(notifictaion)
                    .setDefaults(Notification.DEFAULT_ALL).setSmallIcon(androidx.transition.R.drawable.notification_icon_background);

            notificationManager.notify(0,builder.build());

        }

    }
public void back(View view)
{
    Intent intent = new Intent(chatRoom.this,chat.class);
    startActivity(intent);
    finish();
}


}