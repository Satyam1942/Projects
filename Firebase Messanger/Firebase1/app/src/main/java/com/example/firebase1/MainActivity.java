package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    EditText email;
    EditText password;
    int noOfTaps=0;
    ImageButton imgb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            imgb = findViewById(R.id.showPassword);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
    public   void login(View view)
    {
        if(TextUtils.isEmpty(email.getText().toString()))
        {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password.getText().toString()))
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    mUser = mAuth.getCurrentUser();
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,PortalHome.class);
                    intent.putExtra("authToken",email.getText().toString().substring(0,email.getText().toString().length()-4));
                    startActivity(intent);
                    finish();

                }
                else {

                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onRegister(View view)
    {
        Intent intent = new Intent(this,Register.class);

        startActivity(intent);

    }
    public void showPassword(View view)
    {   if(noOfTaps%2==0) {
        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        imgb.setImageResource(android.R.drawable.ic_lock_lock);
    noOfTaps++;
    }
    else{
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        imgb.setImageResource(android.R.drawable.ic_partial_secure);
        noOfTaps++;

    }

    }
}