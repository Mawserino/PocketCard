package com.example.pocketcard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pocketcard.R;
import com.example.pocketcard.model.userModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public class Qr_profile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private TextView Name;

    private TextView userName;
    private ImageView userQr;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private String signGoogle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_code);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        drawerLayout = findViewById(R.id.drawer_Qr);
        navigationView = findViewById(R.id.nav_Qr);
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
                        startActivity(new Intent(Qr_profile.this,HomePage.class));
                        break;
                    }
                    case R.id.menu_settings: {
                        Toast.makeText(Qr_profile.this, "settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.edit_card: {
                        startActivity(new Intent(Qr_profile.this, edit_card.class));
                        break;
                    }
                    case R.id.show_card: {
                        startActivity(new Intent(Qr_profile.this, show_card.class));
                        break;
                    }
                    case R.id.menu_profile: {
                        startActivity(new Intent(Qr_profile.this, edit_profile.class));
                        break;
                    }
                    case R.id.menu_logout: {
                        mAuth.signOut();
                        startActivity(new Intent(Qr_profile.this, MainActivity.class));
                        break;
                    }
                    case R.id.user_Qr:
                    {
                        startActivity(new Intent(Qr_profile.this, Qr_profile.class));
                        break;
                    }
                }
                return false;
            }
        });



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("users/" + mUser.getUid());

        userName = findViewById(R.id.userName);
        userQr = findViewById(R.id.userQr);

        generateQr(mUser.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel um = snapshot.getValue(userModel.class);
                userName.setText(um.name);
                Name.setText(um.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generateQr(String s){
        BitMatrix result;
        Bitmap bitmap = null;

        try{
            result = new MultiFormatWriter().encode(s, BarcodeFormat.QR_CODE, 300, 300, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];

            for(int y = 0; y < h; y++){
                int offset = y * w;
                for(int x = 0; x < w; x++){
                    pixels[offset + x] = result.get(x, y) ? getColor(R.color.black) : getColor(R.color.white);
                }
            }

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);

            userQr.setImageBitmap(bitmap);


        }catch(WriterException e){
            e.printStackTrace();
        }
    }





}
