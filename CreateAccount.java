package com.example.user.emergencyapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private Button Createaccount;
    private EditText email1, pass, confirm, phonenumber;
    private TextView Login;
    private ProgressBar Signupprogress;
    private static final String TAG = "Create_Account";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private static final String USER = "user";
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Createaccount = findViewById(R.id.create);
        email1 = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        confirm = findViewById(R.id.confirmpassword);
        phonenumber = findViewById(R.id.phonenumber);
        Signupprogress = findViewById(R.id.signupprogress);
        Login = findViewById(R.id.pagelog);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USER);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, Loginpage.class);
                startActivity(intent);

            }
        });
        Createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String email = email1.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String phone = phonenumber.getText().toString().trim();
        String Confirm = confirm.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            email1.setError("Username is Required!");
            return;

        } else if (TextUtils.isEmpty(password)) {
            pass.setError("Password is required!");
            return;
        } else if (TextUtils.isEmpty(phone)) {
            phonenumber.setError("Phone Number is Required!");
            return;
        } else if (TextUtils.isEmpty(Confirm)) {
            confirm.setError("Confirm Password is required!");
            return;
        } else if (password.length() < 6) {
            pass.setError("Password is not strong enough");
            return;
        } else if (!password.equals(Confirm)) {
            Toast.makeText(CreateAccount.this, "Your Passwords are not corresponding", Toast.LENGTH_LONG).show();
            return;
        } else {
            Signupprogress.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Signupprogress.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                //Send verification link
                                FirebaseUser user1 = mAuth.getCurrentUser();

                                updateUI(user1);
                                Toast.makeText(CreateAccount.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                user1.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                         user = new User(email, password, phone);

                                                mDatabase
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Signupprogress.setVisibility(View.GONE);
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(CreateAccount.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                                                    String PhoneNumber = phonenumber.getText().toString().trim();
                                                    Intent intent = new Intent(CreateAccount.this, Loginpage.class);
                                                    intent.putExtra("Phonenumber", PhoneNumber);
                                                    startActivity(intent);
                                                    startActivity(new Intent(CreateAccount.this, Loginpage.class));
                                                } else {
                                                    Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    email1.setText("");
                                                    pass.setText("");
                                                    phonenumber.setText("");
                                                    confirm.setText("");
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Onfailure:Email not Sent" + e.getMessage());
                                        email1.setText("");
                                        pass.setText("");
                                        phonenumber.setText("");
                                        confirm.setText("");
                                    }
                                });


                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        }

    }
    public void updateUI(FirebaseUser currentUser){
    String keyid = mDatabase.push().getKey();
        mDatabase.child(keyid).setValue(user);
    }

}
