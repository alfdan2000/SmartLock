package edu.utep.cs.cs4330.smartlock;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Freddy on 4/30/2017.
 */

public class WifiConnect extends AsyncTask<Void,Void, Void>{

    String dstAddress;
    int dstPort;
    String response;

    WifiConnect(String addr, int port){
        dstAddress = addr;
        dstPort = port;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            Socket socket = new Socket(dstAddress, dstPort);
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            socket.close();
            response = byteArrayOutputStream.toString("UTF-8");

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        MainActivity.setStatus(response);
        super.onPostExecute(result);
    }

}



