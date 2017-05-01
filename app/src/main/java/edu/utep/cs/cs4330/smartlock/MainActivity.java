package edu.utep.cs.cs4330.smartlock;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Boolean isLock = false;
    Boolean isBluetooth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button unlock_lock = (Button)findViewById(R.id.lock_unlock);
        final Button bluetooth = (Button)findViewById(R.id.bluetooth);
        final Button wifiLock = (Button)findViewById(R.id.wifiLock);
        final TextView ipAddress = (TextView)findViewById(R.id.ipAddress);
        final TextView portNumber = (TextView)findViewById(R.id.portNumber);
        final String lockMessage = "Lock";
        final String unlockMessage = "Unlock";

        if(isBluetooth){
            bluetooth.setText("Bluetooth off");
            bluetooth.setBackgroundColor(Color.GRAY);
        }else{
            bluetooth.setText("Bluetooth on");
            bluetooth.setBackgroundColor(Color.BLUE);
            bluetooth.setTextColor(Color.WHITE);
        }

        if(isLock){
            unlock_lock.setText("Press to Unlock");
            unlock_lock.setBackgroundColor(Color.RED);
            wifiLock.setText("Press to Unlock through WIFI");
            wifiLock.setBackgroundColor(Color.RED);
        }else{
            unlock_lock.setText("Press to Lock");
            unlock_lock.setBackgroundColor(Color.GREEN);
            wifiLock.setText("Press to Lock through WIFI");
            wifiLock.setBackgroundColor(Color.GREEN);
        }

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBluetooth){
                    bluetooth.setText("Bluetooth off");
                    bluetooth.setBackgroundColor(Color.GRAY);
                    isBluetooth = false;
                    BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                    bt.disable();
                }else{
                    bluetooth.setText("Bluetooth on");
                    isBluetooth = true;
                    bluetooth.setBackgroundColor(Color.BLUE);
                    bluetooth.setTextColor(Color.WHITE);
                    BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                    bt.enable();
                }
            }
        });


        unlock_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBluetooth) {
                    if (isLock) {
                        unlock_lock.setText("Press to Unlock");
                        isLock = false;
                        unlock_lock.setBackgroundColor(Color.RED);
                    } else {
                        unlock_lock.setText("Press to Lock");
                        isLock = true;
                        unlock_lock.setBackgroundColor(Color.GREEN);
                    }
                }else{
                    toast("Please Turn On Bluetooth");
                }
            }
        });

        wifiLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((ipAddress.getText()) != null || (portNumber.getText())!= null) {
                    if (isLock) {
                        wifiLock.setText("Press to Unlock");
                        isLock = false;
                        wifiLock.setBackgroundColor(Color.RED);
                        WifiConnect myClientTask = new WifiConnect(
                                ipAddress.getText().toString(),
                                Integer.parseInt(portNumber.getText().toString()));
                        myClientTask.execute(lockMessage);
                    } else {
                        wifiLock.setText("Press to Lock");
                        isLock = true;
                        wifiLock.setBackgroundColor(Color.GREEN);
                        WifiConnect myClientTask = new WifiConnect(
                                ipAddress.getText().toString(),
                                Integer.parseInt(portNumber.getText().toString()));
                        myClientTask.execute(unlockMessage);
                    }
                }else{
                    toast("Please enter WIFI values");
                }
            }
        });
    }

    private boolean isBluetoothEnabled(){
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        return bt != null && bt.isEnabled();
    }
    private void enableBluetooth(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(intent);
    }
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
