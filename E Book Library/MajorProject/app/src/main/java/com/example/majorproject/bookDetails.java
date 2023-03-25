package com.example.majorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class bookDetails extends AppCompatActivity {
String bookId;
TextView bookName,bookDescription,bookAuthor,bookPublisher,bookLanguage,bookPublishingDate,bookNoofPages,bookVolumeId,bookRating;
ImageView bookCover;
Button previewBook,buyBook;
String previewUrl,name,buyUrl;
FrameLayout loadingScreen;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        bookId = getIntent().getStringExtra("volumeId");
        bookName = findViewById(R.id.bookTitle);
        bookDescription = findViewById(R.id.bookDescription);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookPublishingDate = findViewById(R.id.bookPublishingDate);
        bookLanguage = findViewById(R.id.bookLanguage);
        bookNoofPages = findViewById(R.id.bookNoofPages);
        bookVolumeId = findViewById(R.id.bookVolumeId);
        bookRating = findViewById(R.id.bookAverageRating);
        bookCover = findViewById(R.id.bookImage);
        previewBook = findViewById(R.id.previewBook);
        buyBook = findViewById(R.id.buyBook);
        loadingScreen =findViewById(R.id.loadingBar);
        retrieveInfo();
    }

    private void retrieveInfo() {

        loadingScreen.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingScreen.setVisibility(View.GONE);
            }
        },1500);
        String url = "https://www.googleapis.com/books/v1/volumes/"+bookId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if(response!=null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    try {
                        bookName.setText(jsonObject.getJSONObject("volumeInfo").getString("title"));
                        name = bookName.getText().toString();
                    } catch (Exception e) {
                        bookName.setText("NA");
                    }
                    try {
                        bookDescription.setText(Html.fromHtml("Description:\n"+jsonObject.getJSONObject("volumeInfo").getString("description")));
                    } catch (Exception e) {
                        bookDescription.setText("Description:NA");

                    }
                    try {
                        bookAuthor.setText("Lead Author: "+jsonObject.getJSONObject("volumeInfo").getJSONArray("authors").getString(0));
                    } catch (Exception e) {
                        bookAuthor.setText("Lead Author:NA");

                    }
                    try {
                        bookPublisher.setText("Publisher: "+ jsonObject.getJSONObject("volumeInfo").getString("publisher"));
                    } catch (Exception e) {
                        bookPublisher.setText("Publisher: NA");

                    }
                    try {
                        bookPublishingDate.setText("Published Date: "+jsonObject.getJSONObject("volumeInfo").getString("publishedDate"));
                    } catch (Exception e) {
                        bookPublishingDate.setText("Published Date:NA");

                    }

                    try {
                        bookLanguage.setText("Language: "+jsonObject.getJSONObject("volumeInfo").getString("language").toUpperCase());
                    } catch (Exception e) {
                        bookLanguage.setText("Language:NA");

                    }

                    try {
                        bookNoofPages.setText("No of Pages: "+jsonObject.getJSONObject("volumeInfo").getString("pageCount"));
                    } catch (Exception e) {
                        bookNoofPages.setText("No of Pages:NA");

                    }

                    try {
                        bookVolumeId.setText("Book Id: "+bookId);
                    } catch (Exception e) {
                        bookNoofPages.setText("Book Id: NA");

                    }

                    try {
                        bookRating.setText("Average Rating : "+jsonObject.getJSONObject("volumeInfo").getString("averageRating"));
                    } catch (Exception e) {
                        bookRating.setText("Average Rating: NA");
                    }
                    try {
                        previewUrl = jsonObject.getJSONObject("volumeInfo").getString("previewLink");
                    } catch (Exception e) {
                    //    Toast.makeText(bookDetails.this, "Preview URL not Available", Toast.LENGTH_SHORT).show();;
                    }
                    try {
                        buyUrl = jsonObject.getJSONObject("saleInfo").getString("buyLink");
                    } catch (Exception e) {
                    //    Toast.makeText(bookDetails.this, "Buy Url not available", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        if (!jsonObject.getJSONObject("saleInfo").getBoolean("isEbook")) {
                            buyBook.setBackgroundColor(Color.WHITE);
                            buyBook.setClickable(false);
                            buyBook.setText("NOT FOR SALE");
                            buyBook.setTextColor(Color.BLACK);
                            if (jsonObject.getJSONObject("accessInfo").getString("viewability").equals("PARTIAL")) {
                                previewBook.setBackgroundColor(Color.BLUE);
                                previewBook.setClickable(true);
                                previewBook.setText("SAMPLE AVAILABLE");
                            }

                            if (jsonObject.getJSONObject("accessInfo").getString("viewability").equals("ALL_PAGES")) {
                                previewBook.setBackgroundColor(Color.BLUE);
                                previewBook.setClickable(true);
                                previewBook.setText("BOOK AVAILABLE");
                            }
                            if (jsonObject.getJSONObject("accessInfo").getString("viewability").equals("NO_PAGES")) {
                                previewBook.setBackgroundColor(Color.WHITE);
                                previewBook.setClickable(false);
                                previewBook.setText("NO SAMPLE");
                                previewBook.setTextColor(Color.BLACK);
                            }
                        } else {
                            buyBook.setBackgroundColor(Color.BLUE);
                            buyBook.setClickable(true);
                            buyBook.setText("BUY NOW");
                            if(jsonObject.getJSONObject("accessInfo").getString("viewability").equals("PARTIAL"))
                            {
                                previewBook.setBackgroundColor(Color.BLUE);
                                previewBook.setClickable(true);
                                previewBook.setText("SAMPLE AVAILABLE");
                            }
                            if (jsonObject.getJSONObject("accessInfo").getString("viewability").equals("ALL_PAGES")) {
                                previewBook.setBackgroundColor(Color.BLUE);
                                previewBook.setClickable(true);
                                previewBook.setText("BOOK AVAILABLE");
                            }
                            if (jsonObject.getJSONObject("accessInfo").getString("viewability").equals("NO_PAGES")) {
                                previewBook.setBackgroundColor(Color.WHITE);
                                previewBook.setClickable(false);
                                previewBook.setText("NO SAMPLE");
                                previewBook.setTextColor(Color.BLACK);
                            }

                        }
                    }catch (Exception e) {


                    }

                    try {
                        Picasso.get().load("https"+jsonObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail").substring(4)).into(bookCover);
                    } catch (Exception e) {


                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(bookDetails.this, "Error fetching Data", Toast.LENGTH_SHORT).show();
                }
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);
    }
public  void buy(View view)
{

    Toast.makeText(this, "Opening Buying Link", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(buyUrl));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setPackage("com.android,chrome");
    try {
        this.startActivity(intent);
    }
    catch (Exception e)
    {
        intent.setPackage(null);
        this.startActivity(intent);
    }
}

    public  void preview(View view)
    {
        Toast.makeText(this, "Opening Preview Link", Toast.LENGTH_SHORT).show();
       Intent intent =new Intent(bookDetails.this,readingRoom.class);
       previewUrl = "https://www.google.co.in/books/edition/"+ name +"/"+bookId+"?hl=en&gbpv=1";
       intent.putExtra("url",previewUrl);
        startActivity(intent);
    }
}