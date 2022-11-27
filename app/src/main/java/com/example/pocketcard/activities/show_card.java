package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pocketcard.R;
import com.example.pocketcard.model.userModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class show_card extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;

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
        setContentView(R.layout.activity_show_card);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_showcard);
        navigationView = findViewById(R.id.nav_viewshowCard);
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
                        startActivity(new Intent(show_card.this,HomePage.class));
                        break;
                    }
                    case R.id.menu_settings:
                    {
                        Toast.makeText(show_card.this,"settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card:
                    {
                        Toast.makeText(show_card.this,"edit card Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.show_card:
                    {
                        Toast.makeText(show_card.this,"show card Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_profile:
                    {
                        Toast.makeText(show_card.this,"profile Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_logout:
                    {
                        mAuth.signOut();
                        startActivity(new Intent(show_card.this,MainActivity.class));
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

    }
}