package com.example.garbadge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class uploadData extends AppCompatActivity {

    EditText organicWaste,inorganicWaste,comments,time,date;
    ImageButton garbagePhoto;
    DatabaseReference dbrf;
    StorageReference strf;
    String authToken;
    static final int CHOOSE_IMAGE = 1;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        organicWaste =findViewById(R.id.organicWaste);
        inorganicWaste = findViewById(R.id.inorganicWaste);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        comments = findViewById(R.id.comments);
        garbagePhoto = findViewById(R.id.garbagePhoto);
        authToken = getIntent().getStringExtra("authToken");
        dbrf = FirebaseDatabase.getInstance().getReference().child(authToken).child("Data").child(date.getText().toString());
        strf = FirebaseStorage.getInstance().getReference().child("Drivers").child("garbagePhoto").child(authToken);
    }

    public void upload(View view)
    {
        dbrf.child(organicWaste.getText().toString()).child("organicWaste").setValue(organicWaste.getText().toString());
        dbrf.child(inorganicWaste.getText().toString()).child("inorganicWaste").setValue(inorganicWaste.getText().toString());
        dbrf.child(time.getText().toString()).child("time").setValue(time.getText().toString());
        dbrf.child(comments.getText().toString()).child("remarks").setValue(comments.getText().toString());

        strf.child(date.getText().toString()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot != null) {
                    Toast.makeText(uploadData.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
        Toast.makeText(this, "All data uploaded successfully", Toast.LENGTH_SHORT).show();

//    catch(Exception e)
//        {
//            Toast.makeText(this, "ERROR uploading details", Toast.LENGTH_SHORT).show();
//        }
        Intent intent = new Intent(uploadData.this,portalHome.class);

        intent.putExtra("authToken",authToken);
        startActivity(intent);
        finish();

    }

    public void getPhoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            garbagePhoto.setImageBitmap(bitmap);
            imageUri = getImageUri(getApplicationContext(), bitmap);


        }

    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitmap,"Title",null);
        return Uri.parse(path);
    }


    public void  backUploadData (View view)
   {
       Intent intent = new Intent(uploadData.this,portalHome.class);
       intent.putExtra("authToken",authToken);
       startActivity(intent);
       finish();
   }
}