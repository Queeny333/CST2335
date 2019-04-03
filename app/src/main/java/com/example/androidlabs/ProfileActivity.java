package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    //declare variables
    ImageButton mImageButton;
    String previousTyped;
    EditText editEmail;
    Button goToChartButton;
    Button goToToolbarButton;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        //get email address from the previous page
        Intent fromPrevious = getIntent();
        previousTyped = fromPrevious.getStringExtra("typed");

        editEmail = (EditText) findViewById(R.id.inEmail);
        editEmail.setText(previousTyped);

        //give the image button action to take a picture
        mImageButton = (ImageButton) findViewById(R.id.inPicture);
        mImageButton.setOnClickListener(m -> {
            dispatchTakePictureIntent();
        });

        goToChartButton = (Button) findViewById(R.id.gochart);
        goToChartButton.setOnClickListener(b -> {
            Intent nextPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(nextPage);
        });

        goToToolbarButton = (Button) findViewById(R.id.gotoToolbar);
        goToToolbarButton.setOnClickListener(b -> {
            Intent nextPage1 = new Intent(ProfileActivity.this, TestToolbar.class);
            startActivity(nextPage1);
        });

        Button gotoWeatherForecast = (Button) findViewById(R.id.gotoWeatherForecast);
        gotoWeatherForecast.setOnClickListener(b -> {
            Intent weatherpage = new Intent(ProfileActivity.this, WeatherForecast.class);
            startActivity(weatherpage);
        });

        Log.e(ACTIVITY_NAME, "In function: onCreate");
    }


    //the function of take a picture
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //if the picture is good, it will be saved under the name "data"

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }

        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME,"In function: onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In fuction: onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In fuction: onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In fuction: onStart");
    }
}
