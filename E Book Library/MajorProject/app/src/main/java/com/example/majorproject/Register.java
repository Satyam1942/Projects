package com.example.majorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Register extends AppCompatActivity {
FirebaseAuth mAuth;
FirebaseUser mUser;
DatabaseReference dbrf;
StorageReference strf;
int noOfTaps=0;
ImageButton imgb;
EditText emailR, passwordR, confirmPasswordR , nameR ,genderR,ageR,genreRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        passwordR =findViewById(R.id.loginPasswordRegister);
        emailR = findViewById(R.id.loginEmailRegister);
        confirmPasswordR = findViewById(R.id.confirmPasswordRegister);
        imgb = findViewById(R.id.imgb2);
        nameR = findViewById(R.id.nameRegister);
        genderR = findViewById(R.id.genderRegister);
        genreRegister = findViewById(R.id.genreRegister);
        ageR = findViewById(R.id.ageRegister);
        dbrf = FirebaseDatabase.getInstance().getReference().child("Users");
            strf = FirebaseStorage.getInstance().getReference().child("Users").child("profilePhoto");
    }
public void  register(View view) {
    informationStorage inf = new informationStorage();
    inf.setEmail(emailR.getText().toString());
    inf.setNameR(nameR.getText().toString());
    inf.setAgeR(ageR.getText().toString());
    inf.setGenderR(genderR.getText().toString());
    inf.setGenre(genreRegister.getText().toString());

    if (TextUtils.isEmpty(emailR.getText().toString()) || TextUtils.isEmpty(passwordR.getText().toString()) || TextUtils.isEmpty(nameR.getText().toString()) || TextUtils.isEmpty(ageR.getText().toString()) || TextUtils.isEmpty(genderR.getText().toString()) || TextUtils.isEmpty(confirmPasswordR.getText().toString()) || TextUtils.isEmpty(genreRegister.getText().toString())) {
        Toast.makeText(this, "Fill all details correctly", Toast.LENGTH_SHORT).show();
    } else {
        if (passwordR.getText().toString().equals(confirmPasswordR.getText().toString())) {

                mAuth.createUserWithEmailAndPassword(emailR.getText().toString(), passwordR.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(Register.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                     if(task.isSuccessful()) {
                         mUser = mAuth.getCurrentUser();
                         dbrf.child(mUser.getUid()).setValue(inf).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 Toast.makeText(Register.this, "Data uploaded Successfully", Toast.LENGTH_SHORT).show();

                                 Intent intent = new Intent(Register.this, Home.class);
                                 startActivity(intent);
                                 finish();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
                     }
                     else{
                         Toast.makeText(Register.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                     }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
            Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_LONG).show();
        }

        }


        }




    public  void backToLogin (View view)
    {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public  void  getPhoto(View view)
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
            strf.child(emailR.getText().toString()).putFile(data.getData());
            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
        }
    }
    public void showPassword(View view) {
        if (noOfTaps % 2 == 0) {
            passwordR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            confirmPasswordR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgb.setImageResource(android.R.drawable.ic_partial_secure);
            noOfTaps++;
        } else {
            passwordR.setTransformationMethod(PasswordTransformationMethod.getInstance());
            confirmPasswordR.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgb.setImageResource(android.R.drawable.ic_lock_lock);

            noOfTaps++;

        }
    }
}