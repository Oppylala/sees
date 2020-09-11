package com.example.user.emergencyapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginpage extends AppCompatActivity {
    private Button Loginbutton;
    private EditText user, pas;
    private TextView account;
    private ProgressBar loginprogress;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        Loginbutton = findViewById(R.id.loginbutton);
        user = findViewById(R.id.loginusername);
        pas = findViewById(R.id.loginpassword);
        account = findViewById(R.id.signup);
        loginprogress = findViewById(R.id.loginprogress);
        myAuth = FirebaseAuth.getInstance();

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = user.getText().toString().trim();
                final String Password = pas.getText().toString().trim();


                if (TextUtils.isEmpty(Username)) {
                    user.setError("Username is required");
                    return;

                } else if (TextUtils.isEmpty(Password)) {
                    pas.setError("Password is required");
                    return;
                }
                else if(Password.length()<6){
                    pas.setError("Password is not strong enough");
                    return;
                }
                else{
                    loginprogress.setVisibility(View.VISIBLE);
                    //Authentication
                    myAuth.signInWithEmailAndPassword(Username,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                if (myAuth.getCurrentUser().isEmailVerified()) {
                                    String Username = user.getText().toString().trim();
                                  //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Loginpage.this, Dashboard.class);
                                    intent.putExtra("Username", Username);
                                    startActivity(intent);
                                    loginprogress.setVisibility(View.INVISIBLE);
                                    user.setText("");
                                    pas.setText("");
                                }

                                else {
                                    Toast.makeText(getApplicationContext(), "Please check your inbox and verify your Email", Toast.LENGTH_LONG).show();
                                    loginprogress.setVisibility(View.INVISIBLE);
                                    user.setText("");
                                    pas.setText("");

                                }
                            }


                            else{
                                Toast.makeText(getApplicationContext(),"Wrong Login Details"+task.getException(),Toast.LENGTH_LONG).show();
                                loginprogress.setVisibility(View.INVISIBLE);
                                user.setText("");
                                pas.setText("");

                            }
                        }
                    });




                }
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loginpage.this, CreateAccount.class);
                startActivity(intent);
            }
        });

    }
}
