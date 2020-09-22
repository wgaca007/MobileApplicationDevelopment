package com.example.homework07;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public static final int MSG_IMAGE_TYPE_RIGHT = 2;
    public static final int MSG_IMAGE_TYPE_LEFT = 3;

    private List<Chat> mChat;
    PrettyTime p = new PrettyTime();
    FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference chatoomsref = db.collection("chatrooms");
    onChatClickListener mListener;

    public MessageAdapter(List<Chat> mChat, onChatClickListener mListener) {
        this.mChat = mChat;
        this.mListener= mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_RIGHT)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right, parent, false);
        else if(viewType == MSG_TYPE_LEFT)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left, parent, false);
        else if(viewType == MSG_IMAGE_TYPE_RIGHT)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_right, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_left, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
         super.getItemViewType(position);

         if(mChat.get(position).getTextimageurl() != null && mChat.get(position).getUserid().equals(user.getUid()))
             return MSG_IMAGE_TYPE_RIGHT;
         else if(mChat.get(position).getTextimageurl() != null && !mChat.get(position).getUserid().equals(user.getUid()))
             return MSG_IMAGE_TYPE_LEFT;

         if(mChat.get(position).getUserid().equals(user.getUid()))
             return MSG_TYPE_RIGHT;
         else
             return MSG_TYPE_LEFT;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        if(chat.getTextimageurl() != null)
            Picasso.get().load(chat.getTextimageurl()).into(holder.textimage);
        else
            holder.message.setText(chat.getMessage());

        holder.sender.setText(chat.getSender());
        holder.timestamp.setText(p.format(chat.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView sender, message, timestamp;
        ImageView textimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sender = itemView.findViewById(R.id.sendername);
            textimage = itemView.findViewById(R.id.textimage);
            message = itemView.findViewById(R.id.textmessage);
            timestamp = itemView.findViewById(R.id.timestamp);
            itemView.setOnCreateContextMenuListener(this);

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onChatLongClickListener(mChat.get(getAdapterPosition()));
                    return true;
                }
            });*/

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 100, 0, "Delete the Message");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onChatLongClickListener(mChat.get(getAdapterPosition()));
                }
            });
        }

    }

    public interface onChatClickListener {

    void onChatLongClickListener(Chat chat);

    }


}
