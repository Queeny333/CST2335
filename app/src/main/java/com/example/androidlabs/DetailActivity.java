package com.example.androidlabs;
/**
 * Title: Lab8_DetailActivity
 * Version: 1.0
 * */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class DetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle bundle = new Bundle();
        bundle.putString("message",getIntent().getStringExtra("message"));
        bundle.putLong("messageId",getIntent().getLongExtra("messageId", 0));
        bundle.putString("s0Rr",getIntent().getStringExtra("s0Rr"));

        FragmentTablet fragmentTablet = new FragmentTablet();
        fragmentTablet.setArguments(bundle);


        getSupportFragmentManager().beginTransaction().replace(R.id.messagedetailsFrameLayout, fragmentTablet).commit();
    }

}