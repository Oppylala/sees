package com.example.user.emergencyapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private static final String TAG = "Profile";
    private Button UserEdit, UserChangePassword;
    private TextView Fullname, Logout;
    private ImageView userImage, Back;
    private ListView li;

    ArrayAdapter<String> adapter;
    DatabaseReference databaseReference;
    List<String> itemlist;
    String uid;
    User users;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserEdit = findViewById(R.id.user_edit);
        UserChangePassword = findViewById(R.id.user_changepassword);
        Fullname = findViewById(R.id.full_name);
        userImage = findViewById(R.id.userimage);
        Logout = findViewById(R.id.logout);
        Back = findViewById(R.id.backicon);
        li = findViewById(R.id.user_info);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        itemlist = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemlist.clear();

                    String email = dataSnapshot.child(uid).child("email").getValue(String.class);
                    String password = dataSnapshot.child(uid).child("password").getValue(String.class);
                    String phone = dataSnapshot.child(uid).child("phone").getValue(String.class);
                    itemlist.add(email);
                    itemlist.add(password);
                    itemlist.add(phone);

                    adapter = new ArrayAdapter<>(Profile.this, android.R.layout.simple_list_item_1, itemlist);
                    li.setAdapter(adapter);

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Error Occur");

            }
        });



        UserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, EditProfile.class));
            }
        });

        UserChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, ChangePassword.class));
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Loginpage.class));
            }
        });
    }
}
