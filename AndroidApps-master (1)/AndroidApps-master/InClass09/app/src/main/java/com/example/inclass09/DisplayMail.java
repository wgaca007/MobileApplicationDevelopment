package com.example.inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMail extends AppCompatActivity {

    TextView sendername, subject, createdat;
    TextView message;
    Button close;

    MessageInfo messageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mail);

        sendername = findViewById(R.id.sendername);
        subject = findViewById(R.id.subjectdisplaymail);
        createdat = findViewById(R.id.createdat);
        message = findViewById(R.id.messagebody);
        close = findViewById(R.id.close);

        if(getIntent() != null && getIntent().getExtras() != null){
            messageInfo = (MessageInfo) getIntent().getSerializableExtra(MainActivity.MESSAGEINNFO);

            sendername.setText("Sender : " + messageInfo.name);
            subject.setText("Subject : " + messageInfo.subject);
            createdat.setText("Created at : " + messageInfo.createdat);
            message.setText(messageInfo.message);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
