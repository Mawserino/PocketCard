package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcard.R;
import com.example.pocketcard.model.cardModel;
import com.example.pocketcard.model.rvModel;
import com.example.pocketcard.model.userModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;

public class cardClick extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef,mRefC;

    ImageView logo;
    TextView NameS,occupationS,numberS,EmailS,locationS,companyNameS;
    Button Fav,Delete;




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
        setContentView(R.layout.activity_card_click);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_cardClick);
        navigationView = findViewById(R.id.nav_cardClick);
        View HeaderView = navigationView.getHeaderView(0);
        Name = HeaderView.findViewById(R.id.tv_nameHeader);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home: {
                        startActivity(new Intent(cardClick.this,HomePage.class));
                        break;
                    }
                    case R.id.menu_settings: {
                        Toast.makeText(cardClick.this, "settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card: {
                        startActivity(new Intent(cardClick.this, edit_card.class));
                        break;
                    }
                    case R.id.show_card: {
                        startActivity(new Intent(cardClick.this, show_card.class));
                        break;
                    }
                    case R.id.menu_profile: {
                        startActivity(new Intent(cardClick.this, edit_profile.class));
                        break;
                    }
                    case R.id.menu_logout: {
                        mAuth.signOut();
                        startActivity(new Intent(cardClick.this, MainActivity.class));
                        break;
                    }
                    case R.id.user_Qr:
                    {
                        startActivity(new Intent(cardClick.this, Qr_profile.class));
                        break;
                    }
                }
                return false;
            }
        });

        NameS = findViewById(R.id.tv_nameshowcard);
        occupationS = findViewById(R.id.tv_occupation);
        numberS = findViewById(R.id.tv_numbershowcard);
        EmailS = findViewById(R.id.tv_emailshowcard);
        locationS = findViewById(R.id.tv_locationshowcard);
        companyNameS = findViewById(R.id.tv_companyNameShowCard);
        logo = findViewById(R.id.iv_logoshowcard);
        Fav = findViewById(R.id.btn_addFav);
        Delete = findViewById(R.id.btn_delete);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel um = snapshot.getValue(userModel.class);
                Name.setText(um.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle intent = getIntent().getExtras();
        String uid = intent.getString("userUID");


        mRef = FirebaseDatabase.getInstance().getReference("users/" + uid);
        mRefC = FirebaseDatabase.getInstance().getReference("usersCard/" + uid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel um = snapshot.getValue(userModel.class);
                NameS.setText(um.getName());
                String imgUrl = um.getProfile();
                numberS.setText(um.getNumber());
                EmailS.setText(um.getEmail());
                try {
                    logo.setImageBitmap(BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    BitmapDrawable ob = new BitmapDrawable(getResources(), BitmapFactory.decodeStream(new URL(imgUrl).openConnection().getInputStream()));
                    logo.setBackground(ob);
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
                    occupationS.setText(cm.getOccupation());
                    locationS.setText(cm.getLocation());
                    companyNameS.setText(cm.getCompanyname());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("usersScan/" + mUser.getUid());
                rvModel rm = new rvModel(NameS.getText().toString(), occupationS.getText().toString(), getIntent().getStringExtra("uid"), "1",companyNameS.getText().toString());
                try {
                    mRef.child(uid).setValue(rm);
                    startActivity(new Intent(cardClick.this, HomePage.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("usersScan/" + mUser.getUid());
                mRef.child(uid).removeValue();
                startActivity(new Intent(cardClick.this, HomePage.class));

            }
        });
    }//onCreate
}