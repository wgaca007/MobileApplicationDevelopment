package com.example.homework07;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements MessageAdapter.onChatClickListener{

    List<Chat> mchat = new ArrayList<Chat>();
    RecyclerView recyclerView;

    EditText textsend;
    MessageAdapter messageAdapter;
    String chatroomid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatoomsref = db.collection("chatrooms");
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    StorageReference textimageref = storageReference.child("images/" +UUID.randomUUID()+ ".jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        textsend = findViewById(R.id.textsend);

        messageAdapter = new MessageAdapter(mchat, this);
        recyclerView.setAdapter(messageAdapter);
        registerForContextMenu(recyclerView);

        if(getIntent() != null && getIntent().getExtras() != null){
            chatroomid = getIntent().getStringExtra("chatroomid");
        }

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat(user.getUid(),user.getDisplayName(), textsend.getText().toString(), new Date(), null);
                chatoomsref.document(chatroomid).collection("chats").add(chat);
                textsend.setText("");
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
                    chat.setChatid(documentSnapshot.getId());
                    mchat.add(chat);
                    messageAdapter.notifyDataSetChanged();
                }

                recyclerView.scrollToPosition(mchat.size()-1);
            }

        });

        setTitle("Chat Room");

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        Chat chat = mchat.get(item.getGroupId());

        final CollectionReference docRef = chatoomsref.document(chatroomid).collection("chats");
        docRef.document(chat.chatid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChatActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return true;
    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_delete_items, menu);
        menu.setHeaderTitle("Do you want to delete the Chat?");

    }
    */


    @Override
    public void onChatLongClickListener(Chat chat) {

        final CollectionReference docRef = chatoomsref.document(chatroomid).collection("chats");
        docRef.document(chat.chatid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChatActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat_items, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UploadTask uploadTask;

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1 || requestCode == 2) {

            if(requestCode == 1) {

                Uri selectedImage = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
               uploadTask = textimageref.putStream(inputStream);
            }
            else{

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytearray = baos.toByteArray();

                uploadTask = textimageref.putBytes(bytearray);
            }

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return textimageref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //Log.d(TAG, "Image Download URL" + task.getResult());
                        Chat chat = new Chat(user.getUid(),user.getDisplayName(), textsend.getText().toString(), new Date(), task.getResult().toString());
                        chatoomsref.document(chatroomid).collection("chats").add(chat);
                    }
                }
            });

        }


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 2);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getTitle().toString()){
            case "attach":
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intent, 1);

            case "camera":
                dispatchTakePictureIntent();
        }
        return true;
    }

}
