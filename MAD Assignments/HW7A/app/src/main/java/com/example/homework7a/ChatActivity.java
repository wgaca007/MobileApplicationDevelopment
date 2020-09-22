package com.example.homework7a;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    List<Chat> mchat = new ArrayList<Chat>();
    RecyclerView recyclerView;

    EditText textsend;
    MessageAdapter messageAdapter;
    String chatroomid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatoomsref = db.collection("chatrooms");
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        textsend = findViewById(R.id.textsend);

        messageAdapter = new MessageAdapter(mchat);
        recyclerView.setAdapter(messageAdapter);

        if(getIntent() != null && getIntent().getExtras() != null){
            chatroomid = getIntent().getStringExtra("chatroomid");
        }

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat(user.getUid(),user.getDisplayName(), textsend.getText().toString(), new Date());
                chatoomsref.document(chatroomid).collection("chats").add(chat);
            }
        });



        final CollectionReference docRef = chatoomsref.document(chatroomid).collection("chats");
        docRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(MainActivity.TAG, "Listen failed.", e);
                    return;
                }

                String source = queryDocumentSnapshots != null && queryDocumentSnapshots.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                mchat.clear();
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                    Log.d(MainActivity.TAG, source);
                    Chat chat = documentSnapshot.toObject(Chat.class);
                    mchat.add(chat);
                    messageAdapter.notifyDataSetChanged();
                }

                recyclerView.scrollToPosition(mchat.size()-1);
            }

        });

    }
}
