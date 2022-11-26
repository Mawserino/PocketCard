package com.example.pocketcard.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pocketcard.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pocketcard.model.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Register extends AppCompatActivity {

    private ActivityResultLauncher<Intent> galleryIntentLauncher;
    private TextInputLayout tilEmail;
    private TextInputLayout tilName;
    private TextInputLayout tilPassword;
    private TextInputLayout tilNumber;
    private Button btnSignup;
    private Button btnCancel;
    private Button btnEditImg;
    private ShapeableImageView iv_prof;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private DatabaseReference mRef;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        btnEditImg = findViewById(R.id.btn_changeImgReg);
        btnSignup = findViewById(R.id.btn_signUp);
        btnCancel = findViewById(R.id.btn_cancelRegis);
        iv_prof = findViewById(R.id.iv_pictureLogo);
        tilEmail = findViewById(R.id.et_emailRegis);
        tilName = findViewById(R.id.et_usernameRegis);
        tilPassword = findViewById(R.id.et_passwordRegis);
        tilNumber = findViewById(R.id.et_numberRegis);

        galleryIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            try {
                                Intent data = result.getData();
                                imageUri = data.getData();
                                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                BitmapDrawable ob = new BitmapDrawable(getResources(), selectedImage);
                                iv_prof.setImageBitmap(selectedImage);
                                //iv_prof.setBackground(ob);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                galleryIntentLauncher.launch(chooserIntent);

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tilName.getEditText().getText().toString().equals(""))
                {
                    tilName.getEditText().setError("Input Name");
                    return;
                }
                else if(tilEmail.getEditText().getText().toString().equals("") || !isValidEmail(tilEmail.getEditText().getText().toString()))
                {
                    tilEmail.getEditText().setError("Input Email");
                    return;
                }
                else if (tilPassword.getEditText().getText().toString().equals("") || tilPassword.getEditText().getText().toString().length() < 6)
                {
                    tilPassword.getEditText().setError("Input password/password must be 6 or more characters");
                    return;
                }
                else if(tilNumber.getEditText().getText().toString().equals("") || tilNumber.getEditText().getText().toString().length() != 11)
                {
                    tilNumber.getEditText().setError("Input Number/Number must be 11 or more numbers");
                    return;
                }
                else if(!(imageUri != null))
                {
                    Toast.makeText(Register.this,"Add Company Logo/Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.createUserWithEmailAndPassword(tilEmail.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //manageUser(mAuth.getCurrentUser());
                                        String uid = mAuth.getCurrentUser().getUid();


                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageRef = storage.getReference();
                                        StorageReference contactImageRef = storageRef.child("UseProfile").child(String.format("%s.png",uid+"Profile"));
                                        iv_prof.setDrawingCacheEnabled(true);
                                        iv_prof.buildDrawingCache();
                                        Bitmap bitmap = ((BitmapDrawable) iv_prof.getDrawable()).getBitmap();
                                        iv_prof.setDrawingCacheEnabled(true);
                                        iv_prof.buildDrawingCache();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] data = baos.toByteArray();
                                        contactImageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        String t = task.getResult().toString();

                                                        userModel um = new userModel(t,tilName.getEditText().getText().toString(), tilNumber.getEditText().getText().toString(), tilEmail.getEditText().getText().toString(), uid);


                                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
                                                        mRef.child(uid).setValue(um);

                                                        mAuth.signOut();
                                                        startActivity(new Intent(Register.this, MainActivity.class));
                                                        finish();

                                                    }
                                                });

                                            }
                                        });

                                    }else{
                                        Toast.makeText(Register.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(Register.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //TODO
                                }
                            });

                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Register.this,MainActivity.class));
                finish();

            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    /*private void createDbInstance(FirebaseUser user){
        String uid = user.getUid();

                                                        userModel um = new userModel(t,tilName.getEditText().getText().toString(), tilPassword.getEditText().getText().toString(), tilEmail.getEditText().getText().toString(), uid);


                                                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
                                                        mRef.child(uid).setValue(um);

                                                        startActivity(new Intent(Register.this, MainActivity.class));
                                                        finish();
    }*/

}