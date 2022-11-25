package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pocketcard.R;
import com.google.android.material.navigation.NavigationView;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

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

        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.nav_viewHome);
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
                        Toast.makeText(HomePage.this,"Home Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_settings:
                    {
                        Toast.makeText(HomePage.this,"settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_card:
                    {
                        Toast.makeText(HomePage.this,"card Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_profile:
                    {
                        Toast.makeText(HomePage.this,"profile Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.menu_logout:
                    {
                        Toast.makeText(HomePage.this,"logout Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
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
}