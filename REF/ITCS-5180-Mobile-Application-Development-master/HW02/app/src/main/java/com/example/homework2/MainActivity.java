package com.example.homework2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //Passwords:
    ArrayList passwords = new ArrayList<String>();
    ArrayList passwordsAsync= new ArrayList<String>();
    //Handler
    Handler handler;

    //Util Class Instance
    final Util util = new Util();

    //ThreadPool
    ExecutorService threadPool;


    // Variables:
    int progPC1;
    int progPL1;
    int progPC2;
    int progPL2;

    String[] VALUE_ARRAY = new String[]{"hello", "world"};

    static String PSWD_VALS;

    public static SeekBar seekBarPC1;
    public static SeekBar seekBarPL1;
    public static SeekBar seekBarPC2;
    public static SeekBar seekBarPL2;

    //TextViews:
    public static TextView textViewPC1;

    public static TextView textViewPL1;

    public static TextView textViewPC2;

    public static TextView textViewPL2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Thread Pool
        threadPool = Executors.newFixedThreadPool(2);

        //Intent
        final Intent intent = new Intent(this, Main2Activity.class);

        //Seek Bars:
        seekBarPC1 = (SeekBar) findViewById(R.id.sbPCount1);
        seekBarPL1 = (SeekBar) findViewById(R.id.sbPLen1);
        seekBarPC2 = (SeekBar) findViewById(R.id.sbPCount2);
        seekBarPL2 = (SeekBar) findViewById(R.id.sbPLen2);

        //TextViews:
        textViewPC1 = (TextView) findViewById(R.id.tvPC1);
        textViewPC1.setText("1");
        textViewPL1 = (TextView) findViewById(R.id.tvPL1);
        textViewPL1.setText("7");
        textViewPC2 = (TextView) findViewById(R.id.tvPC2);
        textViewPC2.setText("1");
        textViewPL2 = (TextView) findViewById(R.id.tvPL2);
        textViewPL2.setText("7");

        //Buttons:
        Button buttonGenerate = (Button) findViewById(R.id.buttonGenerate);

        //Progress Dialog
        final ProgressDialog progressDialog;


        //------------------------------------------------//


        //Seek Bar PC1
        seekBarPC1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("values", String.valueOf(progress + 1));
                progPC1 = progress + 1;
                textViewPC1.setText(String.valueOf(progPC1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //SeekBar PL1
        seekBarPL1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("values", String.valueOf(progress + 7));
                progPL1 = progress + 7;
                textViewPL1.setText(String.valueOf(progPL1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //SeekBar PC2
        seekBarPC2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("values", String.valueOf(progress + 1));
                progPC2 = progress + 1;
                textViewPC2.setText(String.valueOf(progPC2));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //SeekBar PL2
        seekBarPL2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("values", String.valueOf(progress + 7));
                progPL2 = progress + 7;
                textViewPL2.setText(String.valueOf(progPL2));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Progress Dialog:
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Generating Passwords...");
        progressDialog.setMax(progPC1);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);


        //Button Generate Pswd
        buttonGenerate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                passwords.clear();
                passwordsAsync.clear();
                final AsyncTask[] asyncTask = new AsyncTask[1];

                progressDialog.setProgress(0);
                progressDialog.setMax(seekBarPC1.getProgress() + seekBarPC2.getProgress() + 2);
                //For threads...
                threadPool.execute(new GeneratePswdThreads(seekBarPC1.getProgress() + 1, seekBarPL1.getProgress() + 7));
                //Handler
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what){

                            case GeneratePswdThreads.STATUS_START:
                                progressDialog.setProgress(0);
                                progressDialog.show();
                                Log.d("values", "Staring generation...");
                                for (int idx = 0; idx < seekBarPC2.getProgress() + 1; idx ++) {
                                    asyncTask[0] = new GeneratePswdAsync(seekBarPC2.getProgress() + 1, seekBarPL2.getProgress() + 7).execute(100);
                                    progressDialog.setProgress(progressDialog.getProgress() + 1);
                                }
                                break;

                            case GeneratePswdThreads.STATUS_PROGRESS:
                                progressDialog.setProgress(msg.getData().getInt(GeneratePswdThreads.PROGRESS_KEY));

//                              for (int idx = 0; idx < seekBarPC2.getProgress() + 1; idx ++){
                                //new GeneratePswdAsync(seekBarPC2.getProgress() + 1, seekBarPL2.getProgress() + 7).execute(100);
//                                Log.d("my_values", "My Message");
//                            }

                                break;

                            case GeneratePswdThreads.STATUS_STOP:

                                while (passwordsAsync.size() != seekBarPC2.getProgress() + 1){
                                    Log.d("demo", "Async Task Running");
                                }
                                asyncTask[0].cancel(true);


                                progressDialog.dismiss();
                                intent.putExtra(PSWD_VALS, passwords);
                                intent.putStringArrayListExtra("Key", passwordsAsync);
                                startActivity(intent);
                                break;

                        }

                        return false;
                    }
                });
            }
        });
    }

    //Class to generate pswd using threads
    class GeneratePswdThreads implements Runnable{

        //Status Messages
        public static final int STATUS_START = 0x00;
        public static final int STATUS_STOP = 0x01;
        public static final int STATUS_PROGRESS = 0x02;
        public static final String PROGRESS_KEY = "progress";

        int pswdCount;
        int pswdLen;

        public GeneratePswdThreads(int pswdCount, int pswdLen) {
            this.pswdCount = pswdCount;
            this.pswdLen = pswdLen;
        }

        @Override
        public void run() {

            Message start_msg = new Message();
            start_msg.what = STATUS_START;
            handler.sendMessage(start_msg);

            for(int idx = 0; idx < pswdCount; idx++){
                Log.d("pswdLen", String.valueOf(pswdLen));
                passwords.add(util.getPassword(pswdLen));
                Log.d("thread", String.valueOf(pswdLen));
                Message message = new Message();
                message.what = STATUS_PROGRESS;

                Bundle bundle = new Bundle();
                bundle.putInt(PROGRESS_KEY, idx + 1);
                message.setData(bundle);
                handler.sendMessage(message);


            }

            Message stop_msg = new Message();
            stop_msg.what = STATUS_STOP;
            handler.sendMessage(stop_msg);
        }

    }

    class GeneratePswdAsync extends AsyncTask<Integer, Integer, Integer>{

        int pswdCount;
        int pswdLen;

        public GeneratePswdAsync(int pswdCount, int pswdLen) {
            this.pswdCount = pswdCount;
            this.pswdLen = pswdLen;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            //for (int idx = 0; idx < pswdCount; idx++){
            passwordsAsync.add(util.getPassword(pswdLen));
            //}
            return null;
        }
    }
}

