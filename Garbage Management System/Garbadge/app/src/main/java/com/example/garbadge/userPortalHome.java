package com.example.garbadge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class userPortalHome extends AppCompatActivity {
TextView userLocation ,nameDriver, phoneDriver;
EditText complaintRegister;
DatabaseReference dbrf;
String authToken;
int i=0;
FusedLocationProviderClient  fusedLocationProviderClient ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portal_home);


        userLocation = findViewById(R.id.yourLocation);
        nameDriver = findViewById(R.id.nameDriver);
        phoneDriver = findViewById(R.id.phoneNoDriver);
        complaintRegister = findViewById(R.id.complaintUser);

        dbrf = FirebaseDatabase.getInstance().getReference();
        getUserLocation();
        getDriverDetails();
    }

    private void getDriverDetails() {

dbrf.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot snap:snapshot.getChildren())
             {
                 if(userLocation.getText().toString().equals(snap.child("PersonalInfo").child("Address").getValue().toString()))
                 {
                  String   key = snap.getKey();
                  authToken = key;
                     //Toast.makeText(userPortalHome.this, authToken, Toast.LENGTH_SHORT).show();
                     try {

                         dbrf.child(authToken).child("PersonalInfo").child("Name").addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 nameDriver.setText(snapshot.getValue().toString());

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });

                         dbrf.child(authToken).child("PersonalInfo").child("PhoneNumber").addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 phoneDriver.setText(snapshot.getValue().toString());

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });
                     }
                     catch (Exception e)
                     {
                         Toast.makeText(userPortalHome.this, "No Garbage Collector for your Location", Toast.LENGTH_SHORT).show();
                         nameDriver.setText("No driver found");
                     }
                 }
                 else{
                     Toast.makeText(userPortalHome.this, "No Garbage Collector for your Location", Toast.LENGTH_SHORT).show();
                     nameDriver.setText("No driver found");
                 }


        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});


    }

    public void getUserLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },5);
        }

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                List<Address> addresses = null;
                @Override
                public void onSuccess(Location location) {
                    if(location!=null)
                    {
                        Geocoder geo = new Geocoder(userPortalHome.this, Locale.getDefault());
                        try {
                            addresses = geo.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            userLocation.setText(addresses.get(0).getAddressLine(0));
                            Log.d("lpg",addresses.get(0).getAddressLine(0));


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        Toast.makeText(userPortalHome.this, "Location = NULL", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }



    public void registerComplaint(View view)
{

    dbrf.child(authToken).child("Complaints").child(String.valueOf(i)).setValue(complaintRegister.getText().toString());
    i++;
    Toast.makeText(this, "Complaint Uploaded", Toast.LENGTH_SHORT).show();
}
public void callNow(View view)
{
    Intent intent = new Intent(Intent.ACTION_DIAL);

  intent.setData(Uri.parse("tel:"+phoneDriver.getText().toString()));
    startActivity(intent);
}

public void backToHome(View view)
{
    Intent intent = new Intent(userPortalHome.this,MainActivity.class);
    startActivity(intent);
    finish();
}
}