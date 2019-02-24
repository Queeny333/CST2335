package com.example.androidlabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    //declare the variables
    private static final String ACTIVITY_NAME = ChatRoomActivity.class.getSimpleName();
    private ListView listView;
    private List<Message> messages;
    private EditText msgEditText;
    private Button ButtonSend, ButtonReceive;
    private ChatDataBaseHelper dbHelper;
    private  ChatAdapter messageAdapter ;
    protected SQLiteDatabase db;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_layout);
        messages = new ArrayList<>();
        listView = findViewById(R.id.chartList);
        ButtonSend = findViewById(R.id.ButtonSend);
        ButtonReceive = findViewById(R.id.ButtonReceive);
        msgEditText = findViewById(R.id.messageEditText);

        messageAdapter = new ChatAdapter(this, 0);
        listView.setAdapter(messageAdapter);

        dbHelper = new ChatDataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = dbHelper.getMessages();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Message m = new Message(cursor.getString(cursor.getColumnIndex(dbHelper.COL_MESSAGE)),
                        (cursor.getString(cursor.getColumnIndex(dbHelper.COL_ROLE)).equals("SEND")) ? Message.Role.SEND : Message.Role.RECIEVE);
                m.setId(cursor.getLong(cursor.getColumnIndex(dbHelper.COL_ID)));
                Log.d(ACTIVITY_NAME, "MESSAGE ID:"+ m.getId());
                messages.add(m);
                cursor.moveToNext();

            }
            messageAdapter.notifyDataSetChanged();
            Log.d(ACTIVITY_NAME, "=====onRestart");

        }
        ButtonSend.setOnClickListener(v -> {
            String message = msgEditText.getText().toString();
            messages.add(new Message(message, Message.Role.SEND));
            dbHelper.insertMessage(message, Message.Role.SEND.toString());
            msgEditText.setText("");

            messageAdapter.notifyDataSetChanged();
        });

        ButtonReceive.setOnClickListener(v -> {
            String message = msgEditText.getText().toString();
            messages.add(new Message(message, Message.Role.RECIEVE));
            dbHelper.insertMessage(message, Message.Role.RECIEVE.toString());
            msgEditText.setText("");
            messageAdapter.notifyDataSetChanged();
        });

        //for debug
        printCursor(cursor);
    }

    protected class ChatAdapter extends ArrayAdapter<Message> {
        public ChatAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        public Message getItem(int position) {
            return messages.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = ChatRoomActivity.this.getLayoutInflater();
            View newView = null;
            Message message = getItem(position);
            if (message.getRole() == Message.Role.SEND) {
                newView = inflater.inflate(R.layout.message_send, null);
            }
            if (message.getRole() == Message.Role.RECIEVE) {
                newView = inflater.inflate(R.layout.message_receive, null);
            }

            TextView msgTextView = newView.findViewById(R.id.messageText);

            msgTextView.setText(message.getMessage());

            return newView;
        }

        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTIVITY_NAME, "=====onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void printCursor(Cursor c) {
/*  •	The database version number
    •	The number of columns in the cursor.
    •	The name of the columns in the cursor.
    •	The number of results in the cursor
    •	Each row of results in the cursor.*/
        Log.d("SQL Database", "Version:" + db.getVersion());
        Log.d("Cursor", "The number of columns in the cursor:" + c.getColumnCount());
        Log.d("Cursor", "The name of the columns in the cursor:" + join(c.getColumnNames()));
        Log.d("Cursor", "The number of results in the cursor:" + c.getCount());
        Log.d("Cursor", "Each row of results in the cursor:");
        if (c.moveToFirst()) {
            int count = 1;
            while (!c.isAfterLast()) {
                Log.d("row" + count++,
                        " id:" + c.getLong(c.getColumnIndex(dbHelper.COL_ID))
                                + "      message:" + c.getString(c.getColumnIndex(dbHelper.COL_MESSAGE))
                                + "      role:" + c.getString(c.getColumnIndex(dbHelper.COL_ROLE)));
                c.moveToNext();

            }


        }
    }

    //help function
    private String join(String[] names){
        StringBuilder sb = new StringBuilder();
        for(String name : names) sb.append(name).append("\t");
        return sb.toString();

    }
}
