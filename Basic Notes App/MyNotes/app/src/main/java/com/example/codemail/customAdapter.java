package com.example.codemail;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class customAdapter extends ArrayAdapter {
    ArrayList<String> headerList;
    Activity context;

    public customAdapter(@NonNull Activity context, ArrayList<String> headerList) {
        super(context, R.layout.custom_list_view);
        this.context = context;
        this.headerList = headerList;
    }

    @Override
    public int getCount() {
        return headerList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        homeActivity home = new homeActivity();
        if (view == null) {
            view = layoutInflater.inflate(R.layout.custom_list_view, null);
        }
        Button tv = view.findViewById(R.id.notesHeading);
        tv.setText(headerList.get(i));
        ImageButton b = view.findViewById(R.id.deleteItem);
            return  view;
    }
}

