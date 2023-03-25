package com.example.garbadge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {
EditText name,age,phoneNo,address,emailId,password;
ImageButton profilePicture;
RadioGroup gender;
    RadioButton rb;
FirebaseAuth mAuth;
FirebaseUser mUser;
DatabaseReference dbrf;
StorageReference strf;
    String authToken;
static final int CHOOSE_IMAGE = 1;
Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        phoneNo = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordReg);
        profilePicture = findViewById(R.id.profilePicture);
        gender = findViewById(R.id.gender);



    }

    public void submit(View view)
    {
        authToken = emailId.getText().toString().substring(0,emailId.getText().toString().length()-4);
        dbrf = FirebaseDatabase.getInstance().getReference().child(authToken).child("PersonalInfo");
        strf = FirebaseStorage.getInstance().getReference().child("Drivers").child("ProfilePicture").child(authToken);
        int index  = gender.getCheckedRadioButtonId();
        if(index==-1)
        {
            Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show();
        }
        else {
            rb = findViewById(index);
        }

        dbrf.child("Name").setValue(name.getText().toString());
        dbrf.child("Age").setValue(age.getText().toString());
        dbrf.child("Gender").setValue(rb.getText().toString());
        dbrf.child("PhoneNumber").setValue(phoneNo.getText().toString());
        dbrf.child("Address").setValue(address.getText().toString());
        dbrf.child("EmailId").setValue(emailId.getText().toString());

        strf.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot!=null)
                {
                    Toast.makeText(Register.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Register.this, "Image is NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ACCOUNT CREATION
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailId.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mUser = mAuth.getCurrentUser();
                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(Register.this,portalHome.class);
              intent.putExtra("authToken",authToken);

                        startActivity(intent);
                        finish();
                }
                else{
                    Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
public void choosePhoto(View view)
{
Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
intent.setType("image/*");
startActivityForResult(intent,CHOOSE_IMAGE);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null   )
        {
          uri = data.getData();
          profilePicture.setImageURI(uri);
        }
    }

    public void backLogin(View view)
    {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}