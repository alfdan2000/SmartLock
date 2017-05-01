package edu.utep.cs.cs4330.smartlock;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Freddy on 4/30/2017.
 */

public class WifiConnect extends AsyncTask<String,Void, Void>{

        String dstAddress;
        int dstPort;
        String response;

        WifiConnect(String addr, int port) {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Socket socket = new Socket(dstAddress, dstPort);

                OutputStream outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(params[0]);

                socket.close();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }

