package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pocketcard.R;
import com.example.pocketcard.model.userModel;
import com.example.pocketcard.model.cardModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
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

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;


public class edit_card extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef,mRefC;

    ImageView companyLogo;
    TextInputLayout occupation, companyName, location;
    TextView occupationE, companyNameE, locationE;
    Button editcard;
    String imgUrl,uid;

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
        setContentView(R.layout.activity_edit_card);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        occupationE = findViewById(R.id.et_occupation);
        companyNameE = findViewById(R.id.et_companyname);
        locationE = findViewById(R.id.et_location);
        companyLogo = findViewById(R.id.iv_pictureLogoEditCard);
        occupation = findViewById(R.id.et_occupationEditcard);
        companyName = findViewById(R.id.et_companynameEditcard);
        location = findViewById(R.id.et_locationEditcard);
        editcard = findViewById(R.id.bt_editProfilecard);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());
        mRefC = FirebaseDatabase.getInstance().getReference("usersCard/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_editcard);
        navigationView = findViewById(R.id.nav_viewEditcard);
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
                        startActivity(new Intent(edit_card.this,HomePage.class));
                        break;
                    }
                    case R.id.edit_card:
                    {
                        Toast.makeText(edit_card.this, "settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.show_card:
                    {
                        startActivity(new Intent(edit_card.this,show_card.class));
                        break;
                    }
                    case R.id.menu_profile:
                    {
                        startActivity(new Intent(edit_card.this,edit_profile.class));
                        break;
                    }
                    case R.id.menu_logout:
                    {
                        mAuth.signOut();
                        startActivity(new Intent(edit_card.this,MainActivity.class));
                        break;
                    }
                    case R.id.user_Qr:
                    {
                        startActivity(new Intent(edit_card.this, Qr_profile.class));
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
                imgUrl = um.getProfile();
                uid = um.getAuid();

                try {
                    companyLogo.setImageBitmap(BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    BitmapDrawable ob = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    companyLogo.setBackground(ob);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRefC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    cardModel cm = snapshot.getValue(cardModel.class);
                    occupationE.setText(cm.getOccupation());
                    locationE.setText(cm.getLocation());
                    companyNameE.setText(cm.getCompanyname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (occupation.getEditText().getText().toString().equals("")) {
                    occupation.getEditText().setError("Input Name");
                    return;
                } else if (companyName.getEditText().getText().toString().equals("")) {
                    companyName.getEditText().setError("Input Number/Number must be 11 or more numbers");
                    return;
                } else if (location.getEditText().getText().toString().equals("")) {
                    location.getEditText().setError("Input Number/Number must be 11 or more numbers");
                }
                else{

                    setProgressDialog();
                    String fav = "1";
                    cardModel cm = new cardModel(occupation.getEditText().getText().toString(),companyName.getEditText().getText().toString(),location.getEditText().getText().toString(),fav,uid);
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("usersCard");
                    try {
                        mRef.child(uid).setValue(cm);

                        startActivity(new Intent(edit_card.this, show_card.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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