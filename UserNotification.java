package com.example.user.emergencyapps;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class UserNotification extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private ListView Notification;
    private ImageView Back;

    FirebaseDatabase saveData;
    DatabaseReference myRef;

    private ArrayList<String> mUserName = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        Notification = findViewById(R.id.usernotification);
        Back = findViewById(R.id.backicon);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


       // myRef = saveData.getReference().child("emmergencyapps");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        Notification.setAdapter(arrayAdapter);


    }


    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Profile:
                startActivity(new Intent(UserNotification.this, Profile.class));
                return true;
            case R.id.myreport:
                startActivity(new Intent(UserNotification.this, ViewMyReport.class));
                return true;
            case R.id.notification:
                startActivity(new Intent(UserNotification.this, UserNotification.class));
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "Thanks for sharing", Toast.LENGTH_LONG).show();
                return true;
            case R.id.support:
                startActivity(new Intent(UserNotification.this, UserSupport.class));
                return true;
            case R.id.guide:
                startActivity(new Intent(UserNotification.this, UserGuide.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(UserNotification.this, Loginpage.class));
                return true;
            default:
                return false;
        }
    }
}
