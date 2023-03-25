package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //LOADING SCREEN

    //MAIN BG

    private TextView currTemp;
    private TextView currLoc;
    private TextView curraddress;
    private TextView humidity,condition,lastUpdated;
    private EditText citySearch;
    private String cityName ;
    private  ImageView logo;
    String imgUrl;
    int isDay;
    ConstraintLayout constraintLayout;
    FrameLayout frameLayout;
    View view ;
    RecyclerView recyclerView;
    ArrayList<WeatherRVModel> arrayList = new ArrayList<>();
    WeartherRVAdapter weartherRVAdapter;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize all variable
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        view = findViewById(R.id.border);
        constraintLayout = findViewById(R.id.Temperature);
        frameLayout = findViewById(R.id.loadingView);
        frameLayout.setVisibility(View.GONE);
        citySearch = (EditText) findViewById(R.id.city_name);
        citySearch.setText("London");
        citySearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {hideKeyboard(view);}
            }
        });
       currTemp = findViewById(R.id.curr_temp);
       currLoc = findViewById(R.id.curr_loc);
       curraddress = findViewById(R.id.place_descpt);
    logo = findViewById(R.id.logo);
    humidity = findViewById(R.id.humidity);
    condition = findViewById(R.id.condition);
    lastUpdated = findViewById(R.id.lastUpdated);
    recyclerView = findViewById(R.id.forecastList);
    recyclerView.setLayoutManager( new  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        askPermission();
        getLocation();

         weartherRVAdapter = new WeartherRVAdapter(arrayList,this);

        recyclerView.setAdapter(weartherRVAdapter);
        // API used  = weather api
    }

    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            },5);
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
    if(location!=null)
    {
        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
        List<Address> addressList = null;
            try{
                addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                citySearch.setText(addressList.get(0).getLocality());
                cityName = addressList.get(0).getLocality();
                getWeatherInfo(cityName);
                getForecastInfo(cityName);

            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                citySearch.setText("London");
                cityName = "London";
            }
    }
    else{

    }
                }
            });
        } else {
            askPermission();
        }


    }

    private void getForecastInfo(String cityName) {


        String url = "https://api.weatherapi.com/v1/forecast.json?key=d21cea5f1538498f8e4185154232602&q="+cityName+"&days=7&aqi=yes";


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONObject("forecast").getJSONArray("forecastday");
                   for (int  i =1;i<3;i++)
                   {           WeatherRVModel weatherRVModel = new WeatherRVModel();
                            //Made object inside otherwise arraylist will override
                       weatherRVModel.setDate(jsonArray.getJSONObject(i).getString("date"));
                       weatherRVModel.setMinTemp(jsonArray.getJSONObject(i).getJSONObject("day").getString(   "mintemp_c"));
                       weatherRVModel.setMaxTemp(jsonArray.getJSONObject(i).getJSONObject("day").getString(   "maxtemp_c"));
                       weatherRVModel.setAvgTemp(jsonArray.getJSONObject(i).getJSONObject("day").getString(   "avgtemp_c"));
                       weatherRVModel.setCondition(jsonArray.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text"));
                        weatherRVModel.setSunRise(jsonArray.getJSONObject(i).getJSONObject("astro").getString("sunrise"));
                       weatherRVModel.setSunSet(jsonArray.getJSONObject(i).getJSONObject("astro").getString("sunset"));
                       weatherRVModel.setImgUrl(jsonArray.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("icon"));
                       arrayList.add(i-1,weatherRVModel);
                       weartherRVAdapter.notifyDataSetChanged();
                   }
                }
                catch(Exception e)
                {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error fetching Forecast", Toast.LENGTH_SHORT).show();

            }
        }
        );

        requestQueue.add(request);


    }

    public  void searchCity(View view){
    hideKeyboard(view);
    String c = citySearch.getText().toString();

    frameLayout.setVisibility(View.VISIBLE);
    Handler handler  = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            frameLayout.setVisibility(View.GONE);
        }
    },2000);

    getWeatherInfo(c);
        getForecastInfo(c);



}
    private void getWeatherInfo(String cityName )
    {

            String url = "https://api.weatherapi.com/v1/forecast.json?key=d21cea5f1538498f8e4185154232602&q="+cityName+"&days=7&aqi=yes";


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request  = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               try {
                   JSONObject obj = new JSONObject(response);
                    currTemp.setText(obj.getJSONObject("current").getString( "temp_c") + "°C");
                    currLoc.setText(obj.getJSONObject("location").getString( "name"));
                    curraddress.setText(obj.getJSONObject("location").getString( "region"));
                    imgUrl = obj.getJSONObject("current").getJSONObject("condition").getString("icon").toString();
                   Picasso.with(MainActivity.this).load("https:"+imgUrl).into(logo);
                    condition.setText(obj.getJSONObject("current").getJSONObject("condition").getString("text"));
                   humidity.setText("Humidity : "+obj.getJSONObject("current").getString("humidity")+"%");
                    lastUpdated.setText("Last Updated at "+ obj.getJSONObject("current").getString("last_updated"));
                    isDay = Integer.parseInt(obj.getJSONObject("current").getString("is_day"));
                    updateUI(isDay);
               }
               catch(Exception e)
               {
                   Toast.makeText(MainActivity.this, "Weather Not Found ", Toast.LENGTH_SHORT).show();

               }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error fetching Data", Toast.LENGTH_SHORT).show();

            }
           }
        );

        requestQueue.add(request);

    }

    public void updateUI(int isDay)
    {
        if(isDay==0)
        {
            constraintLayout.setBackgroundResource(R.drawable.background_night);
            citySearch.setTextColor(Color.WHITE);
            currTemp.setTextColor(Color.WHITE);
            currLoc.setTextColor(Color.WHITE);
            curraddress.setTextColor(Color.WHITE);
            humidity.setTextColor(Color.WHITE);
            condition.setTextColor(Color.WHITE);
            view.setBackgroundColor(Color.WHITE);
            lastUpdated.setTextColor(Color.WHITE);
            frameLayout.setBackgroundColor(Color.WHITE);
        }else{
            constraintLayout.setBackgroundResource(R.drawable.background);
            citySearch.setTextColor(Color.BLACK);
            currTemp.setTextColor(Color.BLACK);
            currLoc.setTextColor(Color.BLACK);
            curraddress.setTextColor(Color.BLACK);
            humidity.setTextColor(Color.BLACK);
            condition.setTextColor(Color.BLACK);

            lastUpdated.setTextColor(Color.BLACK);
            view.setBackgroundColor(Color.BLACK);
            frameLayout.setBackgroundColor(Color.BLACK);
        }

    }

    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}