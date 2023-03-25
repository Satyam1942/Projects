package com.example.garbadge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class portalHome extends AppCompatActivity {

    FirebaseAuth mAuth;
    String authToken;
    StorageReference strf;
    DatabaseReference dbrf;

    TextView name ,age, gender, phoneNumber , address,emailId;
    ImageView profilePicture;
    String source ,destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_home);
        authToken = getIntent().getStringExtra("authToken");
        mAuth = FirebaseAuth.getInstance();
        dbrf = FirebaseDatabase.getInstance().getReference().child(authToken).child("PersonalInfo");
        strf = FirebaseStorage.getInstance().getReference().child("Drivers").child("ProfilePicture").child(authToken);
        name = findViewById(R.id.nameHome);
        age = findViewById(R.id.ageHome);
        gender = findViewById(R.id.genderHome);
        phoneNumber = findViewById(R.id.phoneNoHome);
        address = findViewById(R.id.addressHome);
        emailId = findViewById(R.id.emailHome);
        profilePicture = findViewById(R.id.profilePictureHome);
        
        loadImage();
        loadContent("Name" ,name);
        loadContent("Age" ,age);
        loadContent("Gender" , gender);
        loadContent("PhoneNumber" ,phoneNumber);
        loadContent("Address" ,address);
        loadContent("EmailId" ,emailId);

    }

    private void loadContent(String str,TextView tv) {
        dbrf.child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null)
                {
                 tv.setText(snapshot.getValue().toString());
                }
                else {
                    Toast.makeText(portalHome.this, "Data = NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadImage() {
        strf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

    }

    public void displayMap(View view)
    {
        source =  "Ranchi Municipal Corporation, near, Kutchery Rd, Deputy Para, Ahirtoli, Ranchi, Jharkhand 834001";
        destination =  "Block A4 New Housing Colony, Sail City, Masibari, Ranchi, Jharkhand 834004";
        Uri uri =  Uri.parse("https://www.google.com/maps/dir/"+  source+ "/" + destination);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    public void uploadData(View view)
    {
       Intent intent = new Intent(portalHome.this ,uploadData.class);
       intent.putExtra("authToken",authToken);

        startActivity(intent);
        finish();

    }
    public void complaintsShow(View view)
    {
        Intent intent = new Intent(portalHome.this , showComplaints.class);
        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();
    }
    public  void signOut(View view)
    {
        mAuth.signOut();
        Intent intent = new Intent(portalHome.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}