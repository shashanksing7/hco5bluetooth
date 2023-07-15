package com.example.hco5bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class sendthread extends  Thread{

    private BluetoothSocket mmSocket;
    private OutputStream outputStream=null;
    private int btmsg;
    private Handler handler;
    private static final int DATA_SENT=2;
    private static final int DATA_NOT_SENT=3;
    private static final int ENABLE_START=4;
    private static final  int PAUSE_TO_RESUME=5;
    private static final int  RESUME_TO_PAUSE=6;

    public sendthread(Handler handler, int message, BluetoothSocket socket) {

        mmSocket=socket;
        this.handler=handler;
        btmsg=message;

        try {
            outputStream=mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


  /*  public void sendthread(Handler handler, String message, BluetoothSocket mmsocket){

        this.mmSocket=mmsocket;
        this.handler=handler;
        btmsg=message;

        try {
            outputStream=mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    @Override
    public void run() {

        if(mmSocket!=null && mmSocket.isConnected()){






            try {
                outputStream.write(btmsg);
                Message msg=Message.obtain();
                msg.what=DATA_SENT;
                handler.sendMessage(msg);

                if(btmsg==112){
                    Message cmsg =Message.obtain();
                    cmsg.what=PAUSE_TO_RESUME;
                    handler.sendMessage(cmsg);

                }else  if(btmsg==101){

                    Message cmsg=Message.obtain();
                    cmsg.what=ENABLE_START;
                    handler.sendMessage(cmsg);

                }
                else if(btmsg==114){
                    Message cmsg=Message.obtain();
                    cmsg.what=RESUME_TO_PAUSE;
                    handler.sendMessage(cmsg);

                }


            } catch (IOException e) {
                e.printStackTrace();
                Message msg=Message.obtain();
                msg.what=DATA_NOT_SENT;
                handler.sendMessage(msg);

            }



        }



    }
}
