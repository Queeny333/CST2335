package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
        private EditText userID, passWord;
        private Button login;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userID = findViewById(R.id.email);
        passWord =findViewById(R.id.password);
        login = findViewById(R.id.login);

        //load the SharedPreferences and load the user’s email address under the reservation name you used.
        // If nothing is reserved, use the empty string “” as the default value.
        sp = getSharedPreferences("Default Email", getApplicationContext().MODE_PRIVATE);
        String email = sp.getString("Default Email", "");
        userID.setText(email);

        login.setOnClickListener(e->{
            Intent loginIntent = new Intent(LoginActivity.this, ProfileActivity.class);
            this.startActivity(loginIntent);
        });

    }


    @Override
    protected void onPause(){
        super.onPause();
         sp = getSharedPreferences("Default Email", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor  edit = sp.edit();
        edit.putString("Default Email",userID.getText().toString());
        edit.commit();
    }
}
