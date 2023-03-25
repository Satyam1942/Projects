package com.example.firebase1;

import static androidx.constraintlayout.motion.widget.Debug.getLoc;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PortalHome extends AppCompatActivity {

DatabaseReference dbrf;
FirebaseAuth mAuth;
String authToken ;
TextView username ,age,email,gender,currLoc ,currTemp ;
ImageView profilePicture;
StorageReference storageReference;
String imgurl;
ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_home);
        username = findViewById(R.id.userName);
        age = findViewById(R.id.age);
        email = findViewById(R.id.emailId);
        gender = findViewById(R.id.gender);
        profilePicture = findViewById(R.id.userImage);
        currLoc = findViewById(R.id.currLocation);
        currTemp = findViewById(R.id.currTemp);
        imgView = findViewById(R.id.weatherCondition);
        storageReference = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");
        mAuth = FirebaseAuth.getInstance();
        dbrf = FirebaseDatabase.getInstance().getReference().child("Users");

getLocn();


        retrieveData("username",username);
        retrieveData("age",age);
        retrieveData("emailId",email);
        retrieveData("gender",gender);
        storageReference.child(authToken).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(PortalHome.this).load(uri).into(profilePicture);
            }
        });

    }


private void  getLocn()
{
    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION

        },5);
    }
if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Geocoder geo = new Geocoder(PortalHome.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currLoc.setText(addresses.get(0).getLocality());
                        String url = "https://api.weatherapi.com/v1/current.json?key=d21cea5f1538498f8e4185154232602&q=" + currLoc.getText().toString() + "&aqi=no\n";

                        RequestQueue requestQueue = Volley.newRequestQueue(PortalHome.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    currTemp.setText(jsonObject.getJSONObject("current").getString("temp_c") + "°C");
                                    imgurl = jsonObject.getJSONObject("current").getJSONObject("condition").getString("icon").toString();
                                    Picasso.with(PortalHome.this).load("https:"+imgurl).into(imgView);

                                } catch (Exception e) {
                                    Toast.makeText(PortalHome.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(PortalHome.this, "Error Loading Weather", Toast.LENGTH_SHORT).show();
                            }
                        });

                            requestQueue.add(stringRequest);


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(PortalHome.this, "Location = NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

    private void retrieveData(String data, TextView tv) {

      dbrf.child(authToken).child(data).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists())
              {
                  String data = snapshot.getValue().toString();
                  tv.setText(data);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
             }
      });

    }

    public void signOut(View view)
    {
        mAuth.signOut();
        Toast.makeText(this, "Signed out Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
public void mail(View view)
{
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/*");
    startActivity(Intent.createChooser(intent,"Email from..."));
}

    public void chat(View view)
    {

        Intent intent = new Intent(this, chat.class);
        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();
    }
}