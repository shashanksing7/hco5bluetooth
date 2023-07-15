package com.example.hco5bluetooth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button turnon,send,connect,pause,stop;
    TextView text;
    BluetoothAdapter bluetoothAdapter;
    Intent bluetoothenableintent;
    public static int requqestcoeforenable;
    BluetoothDevice hc05=null;
    public final static String mac_address="00:21:09:00:5B:08";
    private final static UUID uuid=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static int CONNECTED=0;
    private final static int NOT_CONNECTED=1;
    private static final int DATA_SENT=2;
    private static final int DATA_NOT_SENT=3;
    private static final int ENABLE_START=4;
    private static final int PAUSE_TO_RESUME=5;
    private static final int  RESUME_TO_PAUSE=6;
    private static int pause_resume=112;


    private  static connectthread connectthread;
    private static  sendthread sendthread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnon=findViewById(R.id.button);
        send=findViewById(R.id.button2);
        connect=findViewById(R.id.button3);
        text=findViewById(R.id.textView2);
        pause=findViewById(R.id.button4);
        stop=findViewById(R.id.button5);


        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        bluetoothenableintent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requqestcoeforenable=1;

        pause.setEnabled(false);
        stop.setEnabled(false);


        turnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnonbluetooth();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startride();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pauseride();

            }


        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopride();


            }
        });





    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case CONNECTED:
                    connect.setText("connected");
                    connect.setEnabled(false);
                    break;

                case NOT_CONNECTED:
                    text.setText("Unable to connect");
                    Toast.makeText(getApplicationContext(),"else also executed",Toast.LENGTH_LONG).show();
                    break;
                case DATA_SENT:
                    text.setText("data sent");
                    Toast.makeText(MainActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                    pause.setEnabled(true);
                    stop.setEnabled(true);
                    send.setEnabled(false);

                    break;
                case DATA_NOT_SENT:
                    text.setText("data sending failed");
                    Toast.makeText(MainActivity.this, "data sending failed", Toast.LENGTH_SHORT).show();
                    break;
                case PAUSE_TO_RESUME:
                    pause.setText("Resume");
                    pause_resume=114;
                    break;
                case ENABLE_START:
                    send.setEnabled(true);
                    break;
                case RESUME_TO_PAUSE:
                    pause.setText("Pause");
                    pause_resume=112;






            }



            return false;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == requqestcoeforenable) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabled", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabling Failed", Toast.LENGTH_LONG).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    ///////////////////////////////

    public  void turnonbluetooth(){

        if(bluetoothAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth not supported",Toast.LENGTH_LONG).show();

        }
        else{
            if(bluetoothAdapter.isEnabled()){
                Toast.makeText(getApplicationContext(),"Bluetooth on",Toast.LENGTH_LONG).show();
            }
            else{
                startActivityForResult(bluetoothenableintent,requqestcoeforenable);
            }
        }

    }
    ///////////////////////////////

    public void connect(){
        connectthread=new connectthread(bluetoothAdapter, hc05, handler, mac_address, uuid);
        connectthread.start();





    }

    public void startride(){

        int msg=115;

        sendthread=new sendthread(handler,msg,connectthread.getsocket());
        sendthread.start();



    }

    public void  pauseride(){
        int msg=pause_resume;
        sendthread=new sendthread(handler,msg,connectthread.getsocket());
        sendthread.start();
    }

    public void stopride(){
        int msg=101;
        sendthread=new sendthread(handler,msg,connectthread.getsocket());
        sendthread.start();

    }
}

