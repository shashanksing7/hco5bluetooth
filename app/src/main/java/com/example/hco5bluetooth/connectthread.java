package com.example.hco5bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.UUID;

public class connectthread extends Thread {

    private BluetoothSocket mmSocket;
    private static final String TAG = "connectthread";
    public boolean AASURANCE_VARIABLE=false;
    public static Handler handler;
    private final  static int CONNECTED=0;
    private final static int NOT_CONNECTED=1;

    @SuppressLint("MissingPermission")

    public connectthread(BluetoothAdapter adapter, BluetoothDevice device, Handler handler, String address, UUID uuid) {


        this.handler=handler;

        device = adapter.getRemoteDevice(address);


        BluetoothSocket temp_socket=null;


        try  {

            temp_socket = device.createRfcommSocketToServiceRecord(uuid);
            Log.d(TAG, "connectThread: socket created");
            AASURANCE_VARIABLE=true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "connectThread: creating socket failed");
        }

        mmSocket=temp_socket;



    }






    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        if (mmSocket!=null&&AASURANCE_VARIABLE==true){

            int counter=0;

            do {


                try {
                    mmSocket.connect();
                    Log.d(TAG, "run: connection sucessful");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: connection failed");
                }

            }while (!mmSocket.isConnected()&&counter==5);

            if (mmSocket.isConnected()){
                Message msg=Message.obtain();
                msg.what=CONNECTED;
                handler.sendMessage(msg);
            }
            else{
                Log.d(TAG, "run:unable to connect ");
                Message msg=Message.obtain();
                msg.what=NOT_CONNECTED;
                handler.sendMessage(msg);
            }


        }
    }

    public BluetoothSocket getsocket(){
        return mmSocket;
    }
}
