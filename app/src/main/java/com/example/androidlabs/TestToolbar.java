package com.example.androidlabs;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.PendingIntent.getActivity;
//import static com.example.androidlabs.R.*;

public class TestToolbar extends AppCompatActivity {
    private String currentMessage = "This is the initial message";
    Toolbar tBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

         tBar = (Toolbar)findViewById(R.id.my_toolbar);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.choice1:
                Toast.makeText(TestToolbar.this, currentMessage, Toast.LENGTH_LONG).show();
                break;

            case  R.id.choice2:
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                LayoutInflater inflater = TestToolbar.this.getLayoutInflater();
                View dialogview = inflater.inflate(R.layout.dialog_view, null);
                builder.setView(dialogview);
                EditText editText = dialogview.findViewById(R.id.message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent resultIntent = new Intent();
                        EditText newMessageEditText = dialogview.findViewById(R.id.message);
                        currentMessage = newMessageEditText.getText().toString();
                        onStop();

                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onStop();
                        //finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

        case R.id.choice3:
                Snackbar sb = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("Yes!", e -> finish());
                sb.show();

                break;

            case R.id.choice4:

                Toast.makeText(TestToolbar.this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();
                break;


        }
        return true;
    }

}
