package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;
import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    //declare the variables
    private ListView listView;
    private List<Message> messages;
    private EditText msgEditText;
    private Button ButtonSend, ButtonReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_layout);
        messages = new ArrayList<>();
        listView = findViewById(R.id.chartList);
        ButtonSend = findViewById(R.id.ButtonSend);
        ButtonReceive = findViewById(R.id.ButtonReceive);
        msgEditText = findViewById(R.id.EditText);

        final ChatAdapter messageAdapter = new ChatAdapter(this, 0);
        listView.setAdapter(messageAdapter);

        ButtonSend.setOnClickListener(v -> {
            messages.add(new Message(msgEditText.getText().toString(), Message.Role.SEND));
            msgEditText.setText("");
        });

        ButtonReceive.setOnClickListener(v -> {
            messages.add(new Message(msgEditText.getText().toString(), Message.Role.RECIEVE));
            msgEditText.setText("");
        });
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


}


