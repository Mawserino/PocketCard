package com.example.pocketcard.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pocketcard.R;

import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;

public class HomePage extends AppCompatActivity {

    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        menu = findViewById(R.id.imgmenuhome);
    }
}