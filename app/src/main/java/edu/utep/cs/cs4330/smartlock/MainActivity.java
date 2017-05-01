package edu.utep.cs.cs4330.smartlock;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Boolean isLock = false;
    Boolean isBluetooth = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button wifiLock = (Button)findViewById(R.id.wifiLock);
        final ImageButton lockDisplay = (ImageButton)findViewById(R.id.checkLock); //Lock for bluetooth
        final ImageButton blueDisplay = (ImageButton)findViewById(R.id.checkBluetooth);//Toggle Bluetooth on/off
        final TextView ipAddress = (TextView)findViewById(R.id.ipAddress);
        final TextView portNumber = (TextView)findViewById(R.id.portNumber);
        final TextView status = (TextView)findViewById(R.id.);
        final String lockMessage = "Lock";
        final String unlockMessage = "Unlock";

        /**Used to change lock image on Lock Button*/
        String uriLock = "@drawable/locked";  // where myresource (without the extension) is the file
        int sourceLock = getResources().getIdentifier(uriLock, null, getPackageName());
        final Drawable lock = getResources().getDrawable(sourceLock);

        /**Used to change unlock image on Lock button*/
        String uriULock = "@drawable/unlocked";  // where myresource (without the extension) is the file
        int sourceULock = getResources().getIdentifier(uriULock, null, getPackageName());
        final Drawable unlock = getResources().getDrawable(sourceULock);

        /**Used to change the On image for bluetooth button*/
        String uriBlueOn = "@drawable/bluetooth_on";  // where myresource (without the extension) is the file
        int sourceBlueOn = getResources().getIdentifier(uriBlueOn, null, getPackageName());
        final Drawable BlueOn = getResources().getDrawable(sourceBlueOn);

        /**Used to change the Off image for bluetooth button*/
        String uriBlueOff = "@drawable/bluetooth_off";  // where myresource (without the extension) is the file
        int sourceBlueOff = getResources().getIdentifier(uriBlueOff, null, getPackageName());
        final Drawable BlueOff = getResources().getDrawable(sourceBlueOff);

        /**Sets buttons to appropriate text depending on if bluetooth is on or not*/
        checkBlueBtn(blueDisplay, BlueOn, BlueOff);

        /**Sets buttons to appropriate text depending on if the lock is unlocked or locked.*/
        checkLockBtn(wifiLock,lockDisplay, lock, unlock);

        blueDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBluetooth){
                    blueDisplay.setBackgroundColor(Color.GRAY);
                    isBluetooth = false;
                    blueDisplay.setImageDrawable(BlueOff);
                   // BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                   // bt.disable();
                }else{
                    blueDisplay.setBackgroundColor(Color.BLUE);
                    isBluetooth = true;
                    blueDisplay.setImageDrawable(BlueOn);
                   // BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
                   // bt.enable();
                }

            }
        });

        lockDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBluetooth) {
                    if (isLock) { //if locked, UNLOCK
                        isLock = false;
                        lockDisplay.setBackgroundColor(Color.GREEN);
                        lockDisplay.setImageDrawable(null);
                        lockDisplay.setImageDrawable(unlock);
                        lockDisplay.setScaleType(ImageView.ScaleType.FIT_END);
                        lockDisplay.setAdjustViewBounds(true);
                        lockDisplay.invalidate();
                        toast("Unlocked");
                    } else {
                        isLock = true;
                        lockDisplay.setBackgroundColor(Color.RED);
                        //lockDisplay.setBackgroundResource(R.drawable.unlocked);
                        lockDisplay.setImageDrawable(null);
                        lockDisplay.setImageDrawable(lock);
                        //lockDisplay.setBackgroundResource(R.drawable.unlocked);
                        lockDisplay.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        lockDisplay.setAdjustViewBounds(true);
                        lockDisplay.invalidate();
                        toast("Locked");
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
                        myClientTask.execute();
                    } else {
                        wifiLock.setText("Press to Lock");
                        isLock = true;
                        wifiLock.setBackgroundColor(Color.GREEN);
                        WifiConnect myClientTask = new WifiConnect(
                                ipAddress.getText().toString(),
                                Integer.parseInt(portNumber.getText().toString()));
                        myClientTask.execute();
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

    private void checkLockBtn(Button wifiLock, ImageButton lockDisplay,Drawable lock, Drawable unlock ){
        if(isLock){
            wifiLock.setText("Press to Unlock through WIFI");
            wifiLock.setBackgroundColor(Color.RED);
            lockDisplay.setBackgroundColor(Color.RED);
            lockDisplay.setImageDrawable(lock);
            wifiLock.invalidate();
            lockDisplay.invalidate();
        }else{
            wifiLock.setText("Press to Lock through WIFI");
            wifiLock.setBackgroundColor(Color.GREEN);
            lockDisplay.setBackgroundColor(Color.GREEN);
            lockDisplay.setImageDrawable(unlock);
            wifiLock.invalidate();
            lockDisplay.invalidate();
        }
    }

    private void checkBlueBtn( ImageButton  blueDisplay, Drawable BlueOn,Drawable BlueOff){
        if(isBluetooth){
            blueDisplay.setBackgroundColor(Color.BLUE);
            blueDisplay.setImageDrawable(BlueOn);
            blueDisplay.invalidate();
        }else{
            blueDisplay.setBackgroundColor(Color.GRAY);
            blueDisplay.setImageDrawable(BlueOff);
            blueDisplay.invalidate();
        }
    }
    public static void setStatus(String message){

    }
}
