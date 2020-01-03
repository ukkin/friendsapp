package com.example.friendsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class friendstat extends AppCompatActivity {


    private CircleImageView frnd_image;
    private TextView username;
    private DatabaseReference Pref = FirebaseDatabase.getInstance("upoads").getReference(),
            Uref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendstat);
        //get data from previous activity when item of listview is clicked using intent
        Intent intent = getIntent();
        String f_no = intent.getStringExtra("user_no");
        String f_name = intent.getStringExtra("user_name");


    }
}