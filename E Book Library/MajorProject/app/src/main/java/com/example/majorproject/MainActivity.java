package com.example.majorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    int noOfTaps = 0;
    ImageButton imgb;
    EditText emailLogin, passwordLogin;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    GoogleSignInClient googleSignInClient;
    Button forgotPassword, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgb = findViewById(R.id.imgb);
        emailLogin = findViewById(R.id.loginEmail);
        passwordLogin = findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgotPassword);
        register = findViewById(R.id.register);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

//        if(mUser!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, Home.class);
//            startActivity(intent);
//            finish();
//        }
    }

    public void login(View view) {
        if (TextUtils.isEmpty(emailLogin.getText().toString())) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwordLogin.getText().toString())) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                mUser = mAuth.getCurrentUser();
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void loginGoogle(View view) {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                    mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            mUser = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(this, e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void forgotPassword(View view) {

        forgotPassword.setTextColor(Color.BLUE);
        if (TextUtils.isEmpty(emailLogin.getText().toString())) {
            Toast.makeText(this, "Enter Email and press me again to get password change link", Toast.LENGTH_SHORT).show();
            forgotPassword.setTextColor(Color.RED);
            return;
        }
        mAuth.sendPasswordResetEmail(emailLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Password Change Link has been sent to " + emailLogin.getText().toString(), Toast.LENGTH_LONG).show();

                forgotPassword.setTextColor(Color.BLACK);
            }
        });
    }

    public void register(View view) {
        register.setTextColor(Color.BLUE);
        forgotPassword.setTextColor(Color.BLACK);
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
        finish();
    }

    public void showPassword(View view) {
        if (noOfTaps % 2 == 0) {
            passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgb.setImageResource(android.R.drawable.ic_partial_secure);
            noOfTaps++;
        } else {
            passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgb.setImageResource(android.R.drawable.ic_lock_lock);

            noOfTaps++;

        }
    }
}