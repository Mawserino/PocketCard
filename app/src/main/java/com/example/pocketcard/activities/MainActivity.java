package com.example.pocketcard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pocketcard.R;

public class MainActivity extends AppCompatActivity {

    private Button login;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        register = findViewById(R.id.register);
        login = findViewById(R.id.btLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomePage.class));
            }
        });
    }

    public void register(View view){
        startActivity(new Intent(MainActivity.this,Register.class));
    }
}