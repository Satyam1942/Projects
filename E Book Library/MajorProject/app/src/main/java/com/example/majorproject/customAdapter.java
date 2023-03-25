package com.example.majorproject;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder> {
    ArrayList<bookModel> arrayList;
    Activity context;

    customAdapter(Activity context, ArrayList<bookModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public customAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.ViewHolder holder, int position) {
      //  Toast.makeText(context, "I am in bindView", Toast.LENGTH_SHORT).show();

        holder.nameOfBook.setText(arrayList.get(position).getNameOfBook());
        holder.publisherOfBook.setText(arrayList.get(position).getPublisherOfBook());
        holder.cost.setText(arrayList.get(position).getCost());
        holder.publishingYear.setText("Date of publishing: "+arrayList.get(position).getPublishingYear());
        holder.noOfPages.setText("No of Pages: " + arrayList.get(position).getNoOfPages());

       // Toast.makeText(context,arrayList.get(position).getImgUri() , Toast.LENGTH_SHORT).show();
        Picasso.get().load( "https"+arrayList.get(position).getImgUri()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,bookDetails.class);
                intent.putExtra("volumeId",arrayList.get(holder.getAdapterPosition()).getBookVolumeId());
               context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView nameOfBook , publisherOfBook , cost,publishingYear, noOfPages;
        ImageView img;

        public ViewHolder(@NonNull View view) {
            super(view);

            nameOfBook = view.findViewById(R.id.bookName);
            publisherOfBook= view.findViewById(R.id.publisherName);
            cost = view.findViewById(R.id.cost);
            noOfPages = view.findViewById(R.id.noOfPages);
            publishingYear =view.findViewById(R.id.publishingYear);
            img = view.findViewById(R.id.bookCover);
        }
    }

}