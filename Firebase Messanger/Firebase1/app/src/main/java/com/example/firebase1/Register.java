package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class Register extends AppCompatActivity {

        FirebaseAuth mAuth;
        FirebaseUser mUser;
        int noOfTaps=0;
        ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view)
    {
        TextView emailReg = findViewById(R.id.emailReg);
        TextView passwordReg = findViewById(R.id.passwordReg);
        if(TextUtils.isEmpty(emailReg.getText().toString()))
        {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(passwordReg.getText().toString()))
        {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        }

        mAuth.createUserWithEmailAndPassword(emailReg.getText().toString(),passwordReg.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this,FillForm.class);
                    startActivity(intent);


                }
                else {
                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onLogin(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void showPassword(View view)

    {
        TextView passwordReg = findViewById(R.id.passwordReg);
        imageButton = findViewById(R.id.showPasswordReg);
        if(noOfTaps%2==0) {
        passwordReg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        imageButton.setImageResource(android.R.drawable.ic_lock_lock);
        noOfTaps++;
    }
    else{
        passwordReg.setTransformationMethod(PasswordTransformationMethod.getInstance());
        imageButton.setImageResource(android.R.drawable.ic_partial_secure);
        noOfTaps++;

    }

    }
}