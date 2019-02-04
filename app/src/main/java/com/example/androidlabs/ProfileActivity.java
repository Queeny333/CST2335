package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.androidlabs.R;

public class ProfileActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText name, email;
    ImageButton imgBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        //instantialize components from layout file
        name = findViewById(R.id.profile_edit_text_name);
        email = findViewById(R.id.profile_edit_text_email);
        imgBT = findViewById(R.id.imgButton);

        imgBT.setOnClickListener( v ->{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle ex = data.getExtras();
            Bitmap bitmap = (Bitmap) ex.get("data");
            imgBT.setImageBitmap(bitmap);
        }
    }
}
