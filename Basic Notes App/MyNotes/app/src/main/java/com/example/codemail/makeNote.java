package com.example.codemail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;

public class makeNote extends AppCompatActivity {
    EditText header;

    int MIC_TO_TEXT = 1;
    int CAMERA_CLICK = 2;
    int UPLOAD_TEXT = 3;


    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    ArrayList<String> encodedImage = new ArrayList<>();

    Spannable spannableString;
String auth;
int curr_Auth;
Boolean sameFile = false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);

        textInputEditText = findViewById(R.id.mainText);
        textInputLayout = findViewById(R.id.editableLayout);

            header = findViewById(R.id.headingNote);
        auth = getIntent().getStringExtra("auth");
        curr_Auth = getIntent().getIntExtra("curr_auth",-1);
        sharedPreferences = getSharedPreferences(auth,MODE_PRIVATE);
        if(sharedPreferences.contains("initialized")) {
            sameFile=true;
            header.setText(sharedPreferences.getString("headingText", ""));
            textInputEditText.setText(sharedPreferences.getString("bodyText", ""));
            int sizeOfEncodedImageArray = sharedPreferences.getInt("encodedImageSize",0);
            for (int  i=0;i<sizeOfEncodedImageArray;i++) {
                String previouslyEncodedImage = sharedPreferences.getString("image" + String.valueOf(i), "");
                if (!previouslyEncodedImage.equals("")) {
                    ImageView img = new ImageView(getApplicationContext());


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        byte[] b = Base64.getDecoder().decode(previouslyEncodedImage);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
                        img.setImageBitmap(bitmap);
                    }
                    img.setPadding(10, 20, 0, 0);
                    textInputLayout.addView(img, 500, 500);


                }
            }

        }

    }

    public void uploadObject(View view)

    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,UPLOAD_TEXT);
    }

    public  void clickPhoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_CLICK);
    }


    public void startMic(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to Text");
        try {
            startActivityForResult(intent, MIC_TO_TEXT);
        }
        catch (Exception e){
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    }
    }




    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MIC_TO_TEXT && resultCode == RESULT_OK && data!=null)
        {
            ArrayList<String>  result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textInputEditText.append(" "+ result.get(0));
        }

        if(requestCode==CAMERA_CLICK && resultCode == RESULT_OK && data!=null)
        {
            ImageView img = new ImageView(getApplicationContext());
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
            img.setPadding(10,20,0,0);
            textInputLayout.addView(img,500,500);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                 encodedImage.add( Base64.getEncoder().encodeToString(b));
            }
        }
        if(requestCode == UPLOAD_TEXT && resultCode == RESULT_OK && data.getData()!=null)
        {
            ImageView img = new ImageView(getApplicationContext());
            Uri uri = data.getData();
            img.setImageURI(uri);
            img.setPadding(10,20,0,0);
            textInputLayout.addView(img,500,500);

            try {
                InputStream imageStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(bitmap!=null)
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] b = baos.toByteArray();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    encodedImage.add( Base64.getEncoder().encodeToString(b));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }




        }
    }


    public void saveData(View view )
    {  if(sameFile)
    {sharedPreferences = getSharedPreferences(auth , MODE_PRIVATE);
    }else{
        sharedPreferences =getSharedPreferences(String.valueOf(curr_Auth+1),MODE_PRIVATE);
    }

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
if(!TextUtils.isEmpty(header.getText().toString())) {
    myEdit.putString("headingText", header.getText().toString());
}else{
    Toast.makeText(this, "Enter Heading", Toast.LENGTH_SHORT).show();
    return;
}
if(!TextUtils.isEmpty(textInputEditText.getText().toString())) {
    myEdit.putString("bodyText", textInputEditText.getText().toString());
}else{
    Toast.makeText(this, "Enter Text", Toast.LENGTH_SHORT).show();
    return;
}
myEdit.putBoolean("initialized", true);
myEdit.putInt("encodedImageSize",encodedImage.size());
for (int i =0;i<encodedImage.size();i++)
{
    myEdit.putString("image"+ String.valueOf(i) , encodedImage.get(i));
}

        myEdit.apply();

        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, String.valueOf(curr_Auth+1), Toast.LENGTH_SHORT).show();

    }


    public void back(View view)
    {
        Intent intent = new Intent(makeNote.this,homeActivity.class);
        startActivity(intent);
        finish();
    }
}