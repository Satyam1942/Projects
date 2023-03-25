package com.example.majorproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {
    EditText searchBook;
TextView userNameHome ,nameFragment ,ageFragment,genderFragment,emailFragment,genreFragment;
ImageView profilePictureHome;
FirebaseAuth mAuth;
FirebaseUser mUser;
DatabaseReference dbrf;
RecyclerView recyclerView;
customAdapter adp;
ArrayList<bookModel> arrayList = new ArrayList<>();

FrameLayout loadindScreen;
StorageReference strf;
LinearLayout  layout;
    final String[] query = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layout = findViewById(R.id.linearLayout);
        searchBook = findViewById(R.id.searchBooks);
        userNameHome = findViewById( R.id.userNameHome);
        profilePictureHome = findViewById(R.id.profilePictureHome);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dbrf= FirebaseDatabase.getInstance().getReference();
        mAuth =FirebaseAuth.getInstance();
        loadindScreen =findViewById(R.id.loadingBar);
        strf = FirebaseStorage.getInstance().getReference().child("Users").child("profilePhoto");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adp = new customAdapter(this,arrayList);

        recyclerView.setAdapter(adp);

        dbrf.child("Users").child(mUser.getUid()).child("genre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // Toast.makeText(Home.this, "Hello", Toast.LENGTH_SHORT).show();
                if(snapshot.exists())

                { //Toast.makeText(Home.this, "Hello", Toast.LENGTH_SHORT).show();

                    query[0] = snapshot.getValue().toString();
                    getBookRequest(query[0]);

                    //  Toast.makeText(Home.this, query[0], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        retrieveInfo();
    //    Toast.makeText(Home.this, query[0], Toast.LENGTH_SHORT).show();


    }
public void searchBook(View view)
{
    hidekeyboard();
    loadindScreen.setVisibility(View.VISIBLE);
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            loadindScreen.setVisibility(View.GONE);
        }
    },3000);
 String q = searchBook.getText().toString();
   // Toast.makeText(this, q, Toast.LENGTH_SHORT).show();
    arrayList.clear();
    adp.notifyDataSetChanged();
 getBookRequest(q);
}

    private void hidekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getBookRequest(String query) {

        //Toast.makeText(Home.this, "I am in getBookRequest", Toast.LENGTH_SHORT).show();
String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
     //   Toast.makeText(Home.this, url, Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                if(response!=null)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            bookModel bookModel = new bookModel();
                            try {
                                bookModel.setBookVolumeId(jsonArray.getJSONObject(i).getString("id"));
                                bookModel.setImgUri(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail").substring(4));
                            }catch (Exception e)
                            {
                                bookModel.setImgUri("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQMAAADCCAMAAAB6zFdcAAAAS1BMVEXMzMx/f3/Q0NB5eXnIyMiVlZWEhITAwMCLi4vR0dF8fHy0tLSjo6OysrLDw8Obm5t1dXWsrKyWlpampqa6urqtra2Ojo6fn59vb28EtcIDAAAFL0lEQVR4nO2bCZejKhBGpRAVUNxQ3///pa9wSUdjejJ9Mp1ovnvOTBYpZriylAajCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAnAqK0+cQ06ub8lOolM+iPKgEGqQwz0HI4ZgSSAqhnoMQ8pgOYikyTc9AZ0LGr27OjwgOnnP2CA4+ycHU8fcOfIoDits+yZpix8KnOKBc8hoojBT+JuhDHJDilXRCdtuoz3DAvUBckOkm7DMcxFcKxE3YRzig1lw7kOnm8Ec46FcOzGZG+AwHiVg5yOHAtKd3cJsNklrPB8XZHZBXWeNWR6hbzwebBp/OgR6k4XRwkw2uFGzvGp3NwZIQrhMh8tcJwk3MyRxkS683awmdnA8Y4U6dK5NLvga+WbWV0p4vmow05W1NZ3JAqVjNfWsJlHZtW+zdRj+RA7IrA0IkmwbfuYVyIgdUy7UCYZLHWnYaB6vL47+TcBYH1NwqYAnZIzWdxUFvdhTcl0BaX/k7gwOKk30FLKHfWwicMrxILkfO4IDXxPvsSOAFJNxeTZa18wQOyN/rBJOEZptJLbOnsbPDwzvgNPg7BVsJFH9NHXK6kXB4B3tr4obrX9bXuaRsxu8O7oDKPyrgpuZ6DtomUibjXPLYDvS9NfGehJsswghPx3YQ3V0TtxJaHZbEbKe4qQ+9/yB5VAFLqPW0JO51kiM7+BtMfXfyZDOf4UDsd4KZD3HwLXAAB3AAB3AAB3AAB3AAB3AABydzQA/fO3gAc8znWG5/Y/05sj6mg4iKLHkOWXFQBWE/wbtVBAAAAIBfJXZ/WTL90yWAc9eVLsXTO6XfACrkoyXtWFJX9vvMh8pG55eNOlT58dX999P/4b+HrOC/wlbTaN5tOu86Hb/5+jiXZAfS0uXL6XV6e/kYx5QrmmrgKzA/vrpqrvMNCS2jNm1VR7Vqw+/og2o8t9Kq0uVhR874MVo58NbzUf7QqRDTpuFKy8+hZG1wQL5Ug2MHduC6Rwf8L5S3T8O+ntAyLZK2ln1ZiJxc1fq88tSZoksq7tiZLcR49fflQOdCFT27U5m1idJlOOtVuoSWpc6VLmTnleS6TWuTgeuNqO89V/t+EkYHSa5102tdZ9rzW53UJCxRUUWpIa2dXEpGs4NEUyxdVMZaF0KnkrtLEkKJQ/UwOqgLrSOZajNocjJ2FXnBVac3T3q8nqkfcIObnKjOeMAWrTJ1HG4A8bmrk6EsyyqNVmMhDzvSTBppXw8J+0gKnbUcakPo5IC06/I+OAi9X3h2kE91PbwO/RobB3xO+9wndVwtDmxRFHZc37wZA6QfHcQmpV6UXSs05T2Xpzl07gedbFo7O6DJQc91Wfva9u6xdtBrxV2XDI+FMLlV3O4wl9dj0bgKU7zn1k4OXFpx528FT3iy5R40horFgfA8iCp2UBOPmzAWeNRc6noryJrJgRrGfjAkLlWy5SmtKASP86x3rk+mhaGRrW1lSTTwJBhLngSLuDMibMKrCv52Dp3mxKyMfSa9Nsa7rBnnRNG4NNvb5/xqfMOTv+f1seMTP1BUiswXOWnflD6s6Tzgh7koO0oyXiOoC/tQlSPLx2LlwkIajs+hdU1FS04J5XKrG9snOXcVLhFzXfnrWvoNtOQ3y58x01GO53BJ6wd16JI/zXFfidTlcEigloqmt7QUv/vQz3tCTdLV5qh3hZ8EFcPwjjndr3KobgsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMCv8T8P0EKx6h83/QAAAABJRU5ErkJggg==");
                            }
                            try {
                                bookModel.setNameOfBook(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("title"));
                            }catch (Exception e)
                            {
                                bookModel.setNameOfBook("NA");
                            }
                            try{
                            if(jsonArray.getJSONObject(i).getJSONObject("saleInfo").getBoolean("isEbook"))
                            {  if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("PARTIAL"))
                            {       bookModel.setCost(jsonArray.getJSONObject(i).getJSONObject("saleInfo").getJSONObject("listPrice").getString("amount")+ " "+jsonArray.getJSONObject(i)
                                    .getJSONObject("saleInfo").getJSONObject("listPrice").getString("currencyCode") + "\n FREE SAMPLE AVAILABLE");

                            }
                            if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("ALL_PAGES"))
                                {  bookModel.setCost("Book is FREE");
                                }
                                if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("NO_PAGES"))
                                {bookModel.setCost(jsonArray.getJSONObject(i).getJSONObject("saleInfo").getJSONObject("listPrice").getString("amount")+ " "+jsonArray.getJSONObject(i)
                                        .getJSONObject("saleInfo").getJSONObject("listPrice").getString("currencyCode"));
                                }

                            }else{
                                if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("PARTIAL"))
                                {  bookModel.setCost("NOT FOR SALE \n FREE SAMPLE AVAILABLE");
                                }
                                if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("ALL_PAGES"))
                                {  bookModel.setCost("Book is FREE");
                                }

                                if(jsonArray.getJSONObject(i).getJSONObject("accessInfo").getString("viewability").equals("NO_PAGES"))
                                {
                                    bookModel.setCost("Book is unavailable");
                                }
                            }}catch(Exception e)
                           {
                               bookModel.setCost("Book Unavailable");
                           }

                            try {
                                bookModel.setPublisherOfBook(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("publisher"));
                                //bookModel.setNoOfPages(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("pageCount"));
                            }catch(Exception e)
                            {
                                //Toast.makeText(Home.this, e.toString(), Toast.LENGTH_SHORT).show();
                                bookModel.setPublisherOfBook("Publisher : Unknown");
                            }
                            try{
                                bookModel.setPublishingYear(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("publishedDate"));
                            }
                            catch(Exception e)
                            {
                                bookModel.setPublishingYear("NA");
                            }
                            try{
                                bookModel.setNoOfPages(jsonArray.getJSONObject(i).getJSONObject("volumeInfo").getString("pageCount"));
                            }catch (Exception e)
                            {
                                bookModel.setNoOfPages("NA");
                            }

                            arrayList.add(bookModel);
                            adp.notifyDataSetChanged();
                          //  Toast.makeText(Home.this, "I am in loop", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Home.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
requestQueue.add(stringRequest);
    }

    private void retrieveInfo() {

        loadindScreen.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadindScreen.setVisibility(View.GONE);
            }
        },3000);
        strf.child(mUser.getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePictureHome);
            }
        });

  //      Toast.makeText(this, dbrf.child("Users").child(mUser.getUid()).child("nameR").getKey(), Toast.LENGTH_SHORT).show();
       dbrf.child("Users").child(mUser.getUid()).child("nameR").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   userNameHome.setText("Welcome "+ snapshot.getValue().toString());
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               userNameHome.setText(mUser.getDisplayName());
               Picasso.get().load(mUser.getPhotoUrl()).into(profilePictureHome);
           }
       });


    }
public void openSideViewFragment(View view)
{
    FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
    nameFragment = fragmentContainerView.findViewById(R.id.nameFragment);
    ageFragment = fragmentContainerView.findViewById(R.id.ageFragment);
    genderFragment = fragmentContainerView.findViewById(R.id.genderFragment);
    emailFragment = findViewById(R.id.emailFragment);
    genreFragment = findViewById(R.id.genreFragment);
    genreFragment.setText(query[0]);
    retrieveInfoFragment("nameR",nameFragment);
    retrieveInfoFragment("ageR",ageFragment);
    retrieveInfoFragment("genderR",genderFragment);
    retrieveInfoFragment("email",emailFragment);

fragmentContainerView.setVisibility(View.VISIBLE);
}

    private void retrieveInfoFragment(String str, TextView tv) {
        dbrf.child("Users").child(mUser.getUid()).child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null && snapshot.getValue()!=null)
                {
                    tv.setText(snapshot.getValue().toString());
                }else{
                    Toast.makeText(Home.this, "No data in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Error retrieving Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void closeFragment(View view)
    {
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView);
        fragmentContainerView.setVisibility(View.GONE);

    }

    public void signOut(View view)
    {
        mAuth.signOut();
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void editDetails(View view)
    {
        Intent intent =new Intent(this,profile.class);
        intent.putExtra("nameProfile",nameFragment.getText().toString());
        intent.putExtra("ageProfile",ageFragment.getText().toString());
        intent.putExtra("genderProfile",genderFragment.getText().toString());
        intent.putExtra("emailProfile",emailFragment.getText().toString());
        intent.putExtra("genreProfile",genreFragment.getText().toString());
        startActivity(intent);
        finish();


    }
    public void textToSpeech(View view)
    {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to Text");
            try {
                startActivityForResult(intent, 2);
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode == RESULT_OK && data!=null)
        {
            ArrayList<String>  result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchBook.setText(result.get(0));
            searchBook(new View(this));
        }
    }
}