package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class cardScan extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef,mRefC,otheruser;

    ImageView logo;
    TextView NameS,occupationS,numberS,EmailS,locationS,companyNameS;
    Button save;




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
        setContentView(R.layout.activity_card_scan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_cardScan);
        navigationView = findViewById(R.id.nav_cardScan);
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
                        startActivity(new Intent(cardScan.this,HomePage.class));
                        break;
                    }
                    case R.id.menu_settings: {
                        Toast.makeText(cardScan.this, "settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card: {
                        startActivity(new Intent(cardScan.this, edit_card.class));
                        break;
                    }
                    case R.id.show_card: {
                        startActivity(new Intent(cardScan.this, show_card.class));
                        break;
                    }
                    case R.id.menu_profile: {
                        startActivity(new Intent(cardScan.this, edit_profile.class));
                        break;
                    }
                    case R.id.menu_logout: {
                        mAuth.signOut();
                        startActivity(new Intent(cardScan.this, MainActivity.class));
                        break;
                    }
                    case R.id.user_Qr:
                    {
                        startActivity(new Intent(cardScan.this, Qr_profile.class));
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
        save = findViewById(R.id.btn_save);



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

        mRef = FirebaseDatabase.getInstance().getReference("users/" + getIntent().getStringExtra("uid"));
        mRefC = FirebaseDatabase.getInstance().getReference("usersCard/" + getIntent().getStringExtra("uid"));

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otheruser = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("uid"));

                rvModel rm = new rvModel(NameS.getText().toString(), occupationS.getText().toString(), mUser.getUid(), "0");
                Toast.makeText(cardScan.this,NameS.getText().toString()+occupationS.getText().toString(),Toast.LENGTH_LONG).show();
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("usersScan/").child(mUser.getUid());
                mRef.child(mUser.getUid()).setValue(rm);
                startActivity(new Intent(cardScan.this, HomePage.class));

            }
        });
    }



}