package com.example.user.emergencyapps;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reports extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "Reports";
    private ImageView Back, ReportImage, SendReport;
    private Button Camera, Video, Upload;
    private Spinner Station;
    private TextView CrimeType, MyEmail;
    private EditText Details;
    private ProgressBar ReportProgress;


    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int REQUEST_CODE = 234;
    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorageRef;
    private Uri imguri;
    private StorageTask uploadTask;
    private reportcase Reportcae;
    private DatabaseReference dbreff;

    private Handler handler;

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    List<String> itemlist;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Back = findViewById(R.id.backicon);
        ReportImage = findViewById(R.id.reportimage);
        SendReport = findViewById(R.id.sendsupport);
        Camera = findViewById(R.id.btn_camera);
        Video = findViewById(R.id.btn_video);
        Upload = findViewById(R.id.btn_upload);
        Station = findViewById(R.id.station);
        CrimeType = findViewById(R.id.crimetype);
        Details = findViewById(R.id.reportdetails);
        MyEmail = findViewById(R.id.myemail);
        ReportProgress = findViewById(R.id.supportprogress);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        dbreff = FirebaseDatabase.getInstance().getReference("reportcase");

        Reportcae = new reportcase();
        //Load the Items on Spinner
        uploadStation();

        //Collect Crime Type from Dashboard
        Intent intent = getIntent();
        if (intent.hasExtra("crime")) {
            String email = intent.getStringExtra("crime");
            CrimeType.setText(email);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();
        itemlist = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemlist.clear();

                String email = dataSnapshot.child(uid).child("email").getValue(String.class);

                itemlist.add(email);
                MyEmail.setText(email);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error Occur");

            }
        });


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == Upload) {
                    chooseFile();
                } else if (view == SendReport) {

                }
            }
        });


        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String DetailsReport = Details.getText().toString().trim();
                String StationSelected = Station.getSelectedItem().toString();

                if (TextUtils.isEmpty(DetailsReport)) {
                    Details.setError("Details is required");
                } else if (StationSelected.equals("Select Station")) {
                    Toast.makeText(getApplicationContext(), "Please Select Station", Toast.LENGTH_LONG).show();
                } else {
                    // ReportProgress.setVisibility(View.VISIBLE);

                    registerUser();

                    /*
                    String imageid;
                    imageid = System.currentTimeMillis() + "," + getExtension(imguri);
                    Reportcae.setImageid(imageid);
                    Reportcae.setReport(Details.getText().toString().trim());
                    Reportcae.setStation(Station.getSelectedItem().toString().trim());
                    Reportcae.setCrimeType(CrimeType.getText().toString().trim());
                    Reportcae.setMyemail(MyEmail.getText().toString().trim());


                    dbreff.push().setValue(Reportcae);

                    final ProgressDialog progressDialog = new ProgressDialog(Reports.this);
                    progressDialog.setTitle("Uploading Report");
                    progressDialog.show();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            SendReportMessage();
                            //Toast.makeText(getApplicationContext(),"Your Report has been Uploaded",Toast.LENGTH_LONG).show();
                            // finish();
                        }
                    }, 5000);

                    StorageReference Ref = mStorageRef.child(imageid);
                    uploadTask = Ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Your Report has been Uploaded", Toast.LENGTH_LONG).show();
                            //   ReportProgress.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed" + uploadTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            //  ReportProgress.setVisibility(View.INVISIBLE);
                        }
                    });   */
                }

            }
        });

        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });


    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Profile:
                startActivity(new Intent(Reports.this, Profile.class));
                return true;
            case R.id.myreport:
                startActivity(new Intent(Reports.this, ViewMyReport.class));
                return true;
            case R.id.notification:
                startActivity(new Intent(Reports.this, UserNotification.class));
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "Thanks for sharing", Toast.LENGTH_LONG).show();
                return true;
            case R.id.support:
                startActivity(new Intent(Reports.this, UserSupport.class));
                return true;
            case R.id.guide:
                startActivity(new Intent(Reports.this, UserGuide.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(Reports.this, Loginpage.class));
                return true;
            default:
                return false;
        }
    }


    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);
                ReportImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ReportImage.setImageBitmap(photo);
        }
    }

    private void uploadStation() {
        String[] arraySpinner = new String[]{
                "Select Station",
                "Station 1",
                "Station 2",
                "Station 3",
                "Station 4",
                "Station 5",
                "Station 6",
                "Station 7",
                "Station 8",
                "Station 9",
                "Station 10",
                "Station 11"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Station.setAdapter(adapter);
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


   

    }
}








