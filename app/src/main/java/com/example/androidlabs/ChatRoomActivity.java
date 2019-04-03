package com.example.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatRoomActivity extends AppCompatActivity {

    int clickPosition =0;
    private List<Message> messages;
    private ListView listView;
    private Button ButtonSend;
    private EditText messageEditText;
    private SQLiteDatabase db;
    private ChatDataBaseHelper dbOpener;
    ChatAdapter messageAdapter;
    private boolean isTablet;
    private Button ButtonReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_layout);

        messages = new ArrayList<>();
        messageAdapter = new ChatAdapter(this,0);

        listView = findViewById(R.id.chartList);
        ButtonSend = findViewById(R.id.ButtonSend);
        ButtonReceive = findViewById(R.id.ButtonReceive);
        messageEditText = findViewById(R.id.messageEditText);
        isTablet = findViewById(R.id.frameLayout) != null;

        //get a database
        dbOpener= new ChatDataBaseHelper(this);
        db = dbOpener.getWritableDatabase();

        //query all the results from database;
        String[] columns = {ChatDataBaseHelper.COL_ID, ChatDataBaseHelper.COL_MESSAGE, ChatDataBaseHelper.COL_SEND};
        Cursor results = db.query(false, ChatDataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find tge column indices
        int idColIndex = results.getColumnIndex(ChatDataBaseHelper.COL_ID);
        int messageColIndex = results.getColumnIndex(ChatDataBaseHelper.COL_MESSAGE);
        int isSentColIndex = results.getColumnIndex(ChatDataBaseHelper.COL_SEND);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext()){
            String message = results.getString(messageColIndex);
            String send = results.getString(isSentColIndex);
            long id = results.getLong(idColIndex);
            if(send.equals("TRUE"))
                //add the new message to the array list
                messages.add(new Message(id, message,true));
            if(send.equals("FALSE"))
                messages.add(new Message(id, message,false));

        }

        //create an adapter object and send it to the listView
        messageAdapter = new ChatAdapter(this, 0);
        listView.setAdapter(messageAdapter);

        ButtonSend.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();
            // add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string message in the message column
            newRowValues.put(ChatDataBaseHelper.COL_MESSAGE, message);
            newRowValues.put(ChatDataBaseHelper.COL_SEND,"TRUE");

            //insert to the database
            long newId = db.insert(ChatDataBaseHelper.TABLE_NAME,null,newRowValues);

            Message newMessage = new Message(newId, message,true);

            //add the new message to the list
            messages.add(newMessage);
            //update the listView
            messageAdapter.notifyDataSetChanged();

            //clear the EditText fields
            messageEditText.setText("");

        });

        ButtonReceive.setOnClickListener(v -> {
            String message = messageEditText.getText().toString();

            // add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string message in the message column
            newRowValues.put(ChatDataBaseHelper.COL_MESSAGE, message);
            newRowValues.put(ChatDataBaseHelper.COL_SEND,"FALSE");

            //insert to the database
            long newId = db.insert(ChatDataBaseHelper.TABLE_NAME,null,newRowValues);

            //create new Message object
            Message newMessage = new Message(newId, message, false);

            //add the new message to the list
            messages.add(newMessage);
            //update the listView
            messageAdapter.notifyDataSetChanged();

            //clear the EditText fields
            messageEditText.setText("");
        });

        listView.setOnItemClickListener((parent, view, position, id) ->{
            String message = Objects.requireNonNull(messageAdapter.getItem(position)).getMessage();
            long messageId = messageAdapter.getItemId(position);
            String sORr =String.valueOf(messageAdapter.getItem(position).getIsSent());
            clickPosition=position;

            if(isTablet) {
                FragmentTablet fragmentTablet = FragmentTablet.newInstance(true, message, messageId, sORr);

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentTablet).commit();
            }else{
                Intent intent = new Intent(ChatRoomActivity.this, DetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("messageId", messageId);
                intent.putExtra("sORr", sORr);
                intent.putExtra("isTablet", false);
                startActivityForResult(intent, 21);
            }
        });

        printCursor(results);
    }

    public void deleteMessage(int position) {
        db.delete(ChatDataBaseHelper.TABLE_NAME, ChatDataBaseHelper.COL_ID + " = ?", new String[]{String.valueOf(messageAdapter.getItemId(position))});

        messages.remove(position);
        messageAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data){
        Log.e("in onActivityResult: ", "start here---------------");
        if(requestCode == 21 && responseCode == Activity.RESULT_OK){
            deleteMessage((int) data.getLongExtra("messageId",0));
        }
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
            if (message.getIsSent()) {
                newView = inflater.inflate(R.layout.message_send, null);
            }
            if (!(message.getIsSent())) {
                newView = inflater.inflate(R.layout.message_receive, null);
            }
            TextView messageTextView = newView.findViewById(R.id.messageText);
            messageTextView.setText(message.getMessage());
            return newView;
        }
    }


    public void printCursor(Cursor c) {
        Log.d("SQL DATABASE", "VERSION NUMBER: " + db.getVersion());
        Log.d("Cursor", "number of columns: " + c.getColumnCount());
        for(int i=0; i<c.getColumnCount();i++){
            Log.d("Cursor", "name of the columns: " + c.getColumnNames()[i]);
        }
        Log.d("Cursor", "number of results: " + c.getCount());
        Log.d("Cursor", "Each row of results in the cursor: ");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            //int count = 1;
            Log.d("Cursor", " id: " + c.getLong(c.getColumnIndex(dbOpener.COL_ID)) + " message: " + c.getString(c.getColumnIndex(dbOpener.COL_MESSAGE))
                    + " isSENT: " + c.getString(c.getColumnIndex(dbOpener.COL_SEND)));
            c.moveToNext();

        }

    }

    public long getItemId(int position) {
        return position;
    }

}