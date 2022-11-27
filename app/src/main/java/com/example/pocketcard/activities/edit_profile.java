package com.example.pocketcard.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pocketcard.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pocketcard.model.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class edit_profile extends AppCompatActivity {

    private ActivityResultLauncher<Intent> galleryIntentLauncher;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    private TextInputLayout tilNameE;
    private TextInputLayout tilNumberE;
    private TextView NameE, NumberE;
    private Button btnEditImgE,btnEdit;
    private ShapeableImageView iv_profE;
    Uri imageUri = null;
    Dialog dialog;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        galleryIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            try {
                                int h = 250;
                                int w = 250;
                                Intent data = result.getData();
                                imageUri = data.getData();
                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                Bitmap scaled = Bitmap.createScaledBitmap(selectedImage,w,h,true);
                                BitmapDrawable ob = new BitmapDrawable(getResources(), selectedImage);
                                iv_profE.setImageBitmap(scaled);
                                iv_profE.setBackground(ob);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


        NameE = findViewById(R.id.et_UserE);
        NumberE = findViewById(R.id.et_numberE);
        tilNameE = findViewById(R.id.et_usernameRegisEdit);
        tilNumberE = findViewById(R.id.et_numberRegisEdit);
        btnEditImgE = findViewById(R.id.btn_changeImgRegEdit);
        btnEdit = findViewById(R.id.bt_editProfile);
        iv_profE = findViewById(R.id.iv_pictureLogoEdit);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.nav_viewHome);
        View HeaderView = navigationView.getHeaderView(0);
        Name = HeaderView.findViewById(R.id.tv_nameHeader);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                    {
                        startActivity(new Intent(edit_profile.this,HomePage.class));
                        break;
                    }
                    case R.id.menu_settings:
                    {
                        Toast.makeText(edit_profile.this,"settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card:
                    {
                        Toast.makeText(edit_profile.this,"edit card Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.show_card:
                    {
                        Toast.makeText(edit_profile.this,"show card Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_profile:
                    {
                        Toast.makeText(edit_profile.this,"profile Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_logout:
                    {
                        mAuth.signOut();
                        startActivity(new Intent(edit_profile.this,MainActivity.class));
                        break;
                    }
                }
                return false;
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel um = snapshot.getValue(userModel.class);
                Name.setText(um.getName());
                NameE.setText(um.getName());
                NumberE.setText(um.getNumber());
                String imgUrl = um.getProfile();

                try {
                    iv_profE.setImageBitmap(BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    BitmapDrawable ob = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    iv_profE.setBackground(ob);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEditImgE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                galleryIntentLauncher.launch(chooserIntent);

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tilNameE.getEditText().getText().toString().equals(""))
                {
                    tilNameE.getEditText().setError("Input Name");
                    return;
                }
                else if(tilNumberE.getEditText().getText().toString().equals("") || tilNumberE.getEditText().getText().toString().length() != 11)
                {
                    tilNumberE.getEditText().setError("Input Number/Number must be 11 or more numbers");
                    return;
                }
                else if(iv_profE.getDrawable() == null)
                {
                    Toast.makeText(edit_profile.this,"Add Company Logo/Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    setProgressDialog();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference contactImageRef = storageRef.child("UseProfile").child(String.format("%s.png",mUser.getUid()+"Profile"));
                    iv_profE.setDrawingCacheEnabled(true);
                    iv_profE.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) iv_profE.getDrawable()).getBitmap();
                    iv_profE.setDrawingCacheEnabled(true);
                    iv_profE.buildDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    contactImageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            setProgressDialog();
                            Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    setProgressDialog();
                                    String t = task.getResult().toString();

                                    mRef.child("profile").setValue(t);
                                    mRef.child("name").setValue(tilNameE.getEditText().getText().toString());
                                    mRef.child("number").setValue(tilNumberE.getEditText().getText().toString());

                                    startActivity(new Intent(edit_profile.this, HomePage.class));
                                    finish();

                                }
                            });

                        }
                    });
                }



            }
        });


    }

    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }


}