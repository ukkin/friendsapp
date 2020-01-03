package com.example.friendsapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class friendhome extends AppCompatActivity {
    private DatabaseReference RootRef= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendhome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("app_name");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabAccessorAdapter tabsAdapter = new TabAccessorAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
       // Toast.makeText(friendhome.this, ""+ viewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.create_group) {
            RequestNewGroup();
            return true;
        }
    return true;
    }

    private void RequestNewGroup()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(friendhome.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Are u sure You want to create the group?");
        final CheckBox bidmoney=new CheckBox(friendhome.this);
        bidmoney.setText("Bid Money");

         builder.setView(bidmoney);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(bidmoney.isChecked())
                {
                    Toast.makeText(friendhome.this, "true", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(friendhome.this, "not true", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              dialogInterface.cancel();
            }
        });
        builder.show();
    }




    private void CreateNewGroup(final String grpname)
    {       //database changes to be done
            RootRef.child("user").child("unique_id").child("Groups").child(grpname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(friendhome.this,grpname+" created successfully", Toast.LENGTH_SHORT).show();
                   }
                }
            });
    }
}

