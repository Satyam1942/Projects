package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.firebase1.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FillForm extends AppCompatActivity {
EditText name  ,age, email;
RadioGroup gender ;
DatabaseReference dbrf;
FirebaseDatabase mData;
String authToken="";
ImageButton imgbut;
StorageReference strf;
Uri imageUri = null;

public static final int PICK_IMAGE_REQUEST =22;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        name = findViewById(R.id.name);
        age = findViewById(R.id.form_age);
        email = findViewById(R.id.EmailForm);
        gender = findViewById(R.id.genderFormGroup);
        imgbut = findViewById(R.id.profilePhoto);

        mData = FirebaseDatabase.getInstance();
        dbrf = mData.getReference();
        strf = FirebaseStorage.getInstance().getReference();

    }
public void clickPhoto(View view)
{
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,"Select image ..."),PICK_IMAGE_REQUEST);



}

//To request Photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            imageUri = data.getData();
            imgbut.setImageURI(imageUri);


        }

    }

    public  void getInformation(View view)
    {
        RadioButton rb;
           int id= gender.getCheckedRadioButtonId() ;

        if(id!=-1)
        {
        rb = findViewById(id);
        }
        else
        {
            Toast.makeText(this, "Specify your Gender", Toast.LENGTH_SHORT).show();
            return;

        }




        authToken = email.getText().toString().substring(0,email.getText().toString().length()-4);
        if(imageUri !=null)
        {
            strf.child(authToken).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(FillForm.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Image Not uploaded", Toast.LENGTH_SHORT).show();
        }

        dbrf.child("Users").child(authToken).child("username").setValue(name.getText().toString());
        dbrf.child("Users").child(authToken).child("age").setValue(age.getText().toString());
        dbrf.child("Users").child(authToken).child("emailId").setValue(email.getText().toString());
        dbrf.child("Users").child(authToken).child("gender").setValue(rb.getText().toString());



        Toast.makeText(this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,PortalHome.class);
        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();
    }
}