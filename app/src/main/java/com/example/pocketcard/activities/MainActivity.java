package com.example.pocketcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketcard.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button login;
    TextView register;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tilEmail = findViewById(R.id.et_usernamelogin);
        tilPassword = findViewById(R.id.et_passwordlogin);
        mAuth = FirebaseAuth.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        register = findViewById(R.id.register);
        login = findViewById(R.id.btLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tilEmail.getEditText().getText().toString().equals(""))
                {
                    tilEmail.getEditText().setError("Input Email");
                    return;
                }
                else if (tilPassword.getEditText().getText().toString().equals(""))
                {
                    tilPassword.getEditText().setError("Input password");
                    return;
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(tilEmail.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent start = new Intent(MainActivity.this, HomePage.class);
                                        start.putExtra("uid", "NotGoogle");
                                        startActivity(start);

                                    }else {
                                        //TODO
                                    }
                                }
                            }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Wrong Email or password", Toast.LENGTH_LONG).show();
                                }
                            });
                }


            }
        });
    }

    public void register(View view){
        startActivity(new Intent(MainActivity.this,Register.class));
    }
}