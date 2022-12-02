package com.example.pocketcard.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketcard.R;
import com.example.pocketcard.model.rvModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pocketcard.model.userModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.view.MenuItem;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements Myadapter.onContactClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

    private RecyclerView rv_list;
    private ArrayList<rvModel> arrContacts;
    private DatabaseReference mref;

    FloatingActionButton scan;

    ImageButton menu;

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
        setContentView(R.layout.activity_home_page);

        rv_list = findViewById(R.id.rv_menu);
        arrContacts = new ArrayList<>();

        dbCollect();
        loadContactsList();

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
                switch (item.getItemId()) {
                    case R.id.menu_home: {
                        Toast.makeText(HomePage.this, "Home is Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_settings: {
                        Toast.makeText(HomePage.this, "settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card: {
                        startActivity(new Intent(HomePage.this, edit_card.class));
                        break;
                    }
                    case R.id.show_card: {
                        startActivity(new Intent(HomePage.this, show_card.class));
                        break;
                    }
                    case R.id.menu_profile: {
                        startActivity(new Intent(HomePage.this, edit_profile.class));
                        break;
                    }
                    case R.id.menu_logout: {
                        mAuth.signOut();
                        startActivity(new Intent(HomePage.this, MainActivity.class));
                        break;
                    }
                    case R.id.user_Qr:
                    {
                        startActivity(new Intent(HomePage.this, Qr_profile.class));
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        scan = findViewById(R.id.fab);
        IntentIntegrator qrScan = new IntentIntegrator(this);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                qrScan.setPrompt("Scan");
                qrScan.setCameraId(0); //0 - back cam, 1 - front cam
                qrScan.setBeepEnabled(true);
                qrScan.initiateScan();

            }
        });


    }

    private void loadContactsList() {

        Myadapter adapter = new Myadapter(arrContacts, this);
        rv_list = findViewById(R.id.rv_menu);
        rv_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
    }

    private void dbCollect() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        mref = FirebaseDatabase.getInstance().getReference("userContacts");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Log.d(TAG, "onDataChange: ");
                    rvModel user = dataSnapshot.getValue(rvModel.class);
                    arrContacts.add(new rvModel(user.getName(),user.getOccupation2(),user.getEmail(), "0"));
                    loadContactsList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public void onContactClick(int position) {

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
            if(intentResult.getContents() == null){
                //TODO cancelledjohn
            }else {
                Intent start = new Intent(HomePage.this, cardScan.class);
                start.putExtra("uid", intentResult.getContents());
                startActivity(start);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}