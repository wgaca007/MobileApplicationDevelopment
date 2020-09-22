package com.example.noteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayNote extends AppCompatActivity {

    TextView note_text;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        NotesUtil note_to_display = (NotesUtil) getIntent().getSerializableExtra("Key");

        note_text = (TextView)findViewById(R.id.tv_note_text_display_note);
        note_text.setText(note_to_display.note_text.toString());

        close = (Button)findViewById(R.id.btn_close_display_note);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
