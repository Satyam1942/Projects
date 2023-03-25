package com.example.weatherapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeartherRVAdapter extends RecyclerView.Adapter<WeartherRVAdapter.ViewHolder> {

    ArrayList<WeatherRVModel> arrayList;
    Activity context;
    WeartherRVAdapter(ArrayList<WeatherRVModel> arrayList, Activity context)
    {
                this.arrayList = arrayList;
                this.context = context;
    }

    @NonNull
    @Override
    public WeartherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forcast_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeartherRVAdapter.ViewHolder holder, int position) {
        holder.date.setText(arrayList.get(position).getDate());
        holder.maxTemp.setText(arrayList.get(position).getMaxTemp()+ "°C");
        holder.minTemp.setText(arrayList.get(position).getMinTemp()+ "°C");
        holder.avgTemp.setText(arrayList.get(position).getAvgTemp());
        holder.condition.setText(arrayList.get(position).getCondition());
        holder.sunSet.setText(arrayList.get(position).getSunSet());
        holder.sunRise.setText(arrayList.get(position).getSunRise());
        Picasso.with(context).load("https:"+ arrayList.get(position).getImgUrl()).into(holder.weatherStatus);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,minTemp,maxTemp,avgTemp,condition,sunRise,sunSet;
        ImageView weatherStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateForecast);
            minTemp = itemView.findViewById(R.id.tempMinForecast);
            maxTemp = itemView.findViewById(R.id.tempMaxForecast);
            avgTemp = itemView.findViewById(R.id.avgTempForecast);
            condition = itemView.findViewById(R.id.conditionForecast);
            sunRise = itemView.findViewById(R.id.sunRiseForecast);
            sunSet = itemView.findViewById(R.id.sunsetForecast);
            weatherStatus = itemView.findViewById(R.id.weatherStatus);

        }
    }
}

