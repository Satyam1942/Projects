package com.example.garbadge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText token,password,userPhoneNo;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton showPass;
PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

int i=0;
String authToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        password = findViewById(R.id.passwordReg);
        token = findViewById(R.id.driverToken);
        showPass = findViewById(R.id.showPassword);

    }


    public void login(View view)
{       mAuth = FirebaseAuth.getInstance();

        if(TextUtils.isEmpty(token.getText().toString()))
        {
            Toast.makeText(this, "Enter Token", Toast.LENGTH_SHORT).show();
            return;
        }
    if(TextUtils.isEmpty(password.getText().toString()))
    {
        Toast.makeText(this, "Enter Token", Toast.LENGTH_SHORT).show();
        return;
    }
        mAuth.signInWithEmailAndPassword(token.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mUser = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    authToken = token.getText().toString().substring(0,token.getText().toString().length()-4);
                    Intent intent = new Intent(MainActivity.this,portalHome.class);
                     intent.putExtra("authToken",authToken);
                        startActivity(intent);
                        finish();

                }
                else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


}
public void register(View view)
{

 Intent intent = new Intent(this, Register.class);
startActivity(intent);
finish();

}
public void showPassword(View view)
{         if(i%2==0) {
    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    showPass.setImageResource(android.R.drawable.ic_lock_lock);
    i++;
}
else {
    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

    showPass.setImageResource(android.R.drawable.ic_partial_secure);
    i++;

}
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void userLogin(View view)
    {
        Intent intent = new Intent(MainActivity.this,userPortalHome.class);
                startActivity(intent);
                finish();
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber( userPhoneNo.getText().toString())
//                .setTimeout(120L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallbacks).build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Intent intent = new Intent(MainActivity.this,userPortalHome.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//                Toast.makeText(MainActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
//            }
//        };
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.viewMap:
//            //    Intent intent = new Intent(this,displayMap.class);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        }
}