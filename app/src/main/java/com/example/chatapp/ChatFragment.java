package com.example.chatapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private EditText etReceiver, etMessage;
    private Button btnSend;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private ChatDatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        etReceiver = view.findViewById(R.id.etReceiver);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        recyclerView = view.findViewById(R.id.recyclerView);

        dbHelper = new ChatDatabaseHelper(getContext());
        chatMessages = new ArrayList<>();

        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        loadMessages();

        return view;
    }

    private void sendMessage() {
        String receiver = etReceiver.getText().toString();
        String message = etMessage.getText().toString();

        if (TextUtils.isEmpty(receiver) || TextUtils.isEmpty(message)) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.SENDER, "CurrentUser");  // Replace with the actual sender
        values.put(ChatDatabaseHelper.RECEIVER, receiver);
        values.put(ChatDatabaseHelper.MESSAGE, message);

        long id = db.insert(ChatDatabaseHelper.MESSAGE_TABLE, null, values);
        if (id != -1) {
            ChatMessage chatMessage = new ChatMessage("CurrentUser", receiver, message);
            chatMessages.add(chatMessage);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            recyclerView.scrollToPosition(chatMessages.size() - 1);
            etMessage.setText("");
        }
    }

    private void loadMessages() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ChatDatabaseHelper.MESSAGE_TABLE,
                new String[]{ChatDatabaseHelper.SENDER, ChatDatabaseHelper.RECEIVER, ChatDatabaseHelper.MESSAGE},
                null, null, null, null, ChatDatabaseHelper.TIMESTAMP + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String sender = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.SENDER));
                @SuppressLint("Range") String receiver = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.RECEIVER));
                @SuppressLint("Range") String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.MESSAGE));

                ChatMessage chatMessage = new ChatMessage(sender, receiver, message);
                chatMessages.add(chatMessage);
            }
            cursor.close();
            chatAdapter.notifyDataSetChanged();
        }
    }
}
