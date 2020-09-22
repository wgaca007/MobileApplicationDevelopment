package com.example.noteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayNotesAdapter extends RecyclerView.Adapter<DisplayNotesAdapter.ViewHolder>{

    ArrayList<NotesUtil> my_notes = new ArrayList<>();
    AdapterInterface adapterInterface;
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_note, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public DisplayNotesAdapter(ArrayList<NotesUtil> my_notes, AdapterInterface adapterInterface, Context context) {
        this.my_notes = my_notes;
        this.adapterInterface = adapterInterface;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.note_text.setText(my_notes.get(i).note_text);
        viewHolder.ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.onDelete(i);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.onDisplay(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return my_notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView note_text;
        ImageButton ib_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            note_text = (TextView)itemView.findViewById(R.id.tvNoteText);
            ib_delete = (ImageButton)itemView.findViewById(R.id.ibDelete);
        }
    }

    public interface AdapterInterface{
        public void onDelete(int index);
        public void onDisplay(int index);
    }

}
