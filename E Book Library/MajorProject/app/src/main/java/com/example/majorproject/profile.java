package com.example.majorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
EditText nameProfile,genderProfile,ageProfile,genreProfile;
TextView emailProfile;
ImageView profilePictureProfile;
FirebaseUser mUser;
FirebaseAuth mAuth;
DatabaseReference dbrf;
StorageReference strf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameProfile = findViewById(R.id.nameProfile);
        ageProfile = findViewById(R.id.ageProfile);
        genderProfile = findViewById(R.id.genderProfile);
        emailProfile = findViewById(R.id.emailProfile);
        genreProfile = findViewById(R.id.genreProfile);

        nameProfile.setText(getIntent().getStringExtra("nameProfile"));
        ageProfile.setText(getIntent().getStringExtra("ageProfile"));
        genreProfile.setText(getIntent().getStringExtra("genreProfile"));
        genderProfile.setText(getIntent().getStringExtra("genderProfile"));
        emailProfile.setText(getIntent().getStringExtra("emailProfile"));
        dbrf  = FirebaseDatabase.getInstance().getReference().child("Users");
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        strf = FirebaseStorage.getInstance().getReference().child("Users").child("profilePhoto");
    }
    public void saveProfile(View view)
    {
        informationStorage informationStorage = new informationStorage();
        informationStorage.setNameR(nameProfile.getText().toString());
        informationStorage.setAgeR(ageProfile.getText().toString());
        informationStorage.setGenderR(genderProfile.getText().toString());
        informationStorage.setEmail(emailProfile.getText().toString());
        informationStorage.setGenre(genreProfile.getText().toString());
        dbrf.child(mUser.getUid()).setValue(informationStorage);
        Toast.makeText(this, "Data changed successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(profile.this,Home.class);
        startActivity(intent);
        finish();
    }
    public void  deleteAccount(View view)
    {
        strf.child(mUser.getEmail()).delete();
        mAuth.signOut();
        Query query = dbrf.child(mUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null)
                {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(profile.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(profile.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
    public  void  changePictureProfile(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null)
        {
            strf.child(emailProfile.getText().toString()).putFile(data.getData());
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
        }
    }
}