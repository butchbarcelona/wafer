package com.proj.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;
import com.proj.demo.wafer.MainActivity;
import com.proj.wafer.BuildConfig;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public abstract class BaseActivity extends FragmentActivity{

    USBConnectListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsbManager = UsbManager.getInstance(this);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        filter.addAction(Intent.ACTION_UMS_DISCONNECTED);
        registerReceiver(mUsbReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToAccessory();
    }



/*    @Override
    public void onBackPressed() {
        if (mAccessory != null) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else
            finish();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeAccessory();
        unregisterReceiver(mUsbReceiver);
    }


    public static final boolean D = BuildConfig.DEBUG; // This is automatically set when building
    protected static final String TAG = "ADKActivity"; // TAG is used to debug in Android logcat console
    protected static final String ACTION_USB_PERMISSION = "com.tkjelectronics.arduino.blink.led.USB_PERMISSION";

    UsbAccessory mAccessory;
    ParcelFileDescriptor mFileDescriptor;
    protected FileInputStream mInputStream;
    protected FileOutputStream mOutputStream;
    protected UsbManager mUsbManager;
    protected PendingIntent mPermissionIntent;
    protected boolean mPermissionRequestPending;
    ConnectedThread mConnectedThread;

    protected final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = UsbManager.getAccessory(intent);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        openAccessory(accessory);
                    else {
                        if (D)
                            Log.d(TAG, "Permission denied for accessory " + accessory);
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action) || Intent.ACTION_UMS_DISCONNECTED.equals(action)) {
                Log.d(TAG, "Accessory disconnected");
                UsbAccessory accessory = UsbManager.getAccessory(intent);
                if (accessory != null && accessory.equals(mAccessory))
                    closeAccessory();
            }
        }
    };



    protected void openAccessory(UsbAccessory accessory) {
        mFileDescriptor = mUsbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            mAccessory = accessory;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);

            mConnectedThread = new ConnectedThread(this);
            mConnectedThread.start();

            setConnectionStatus(true);

            if (D)
                Log.d(TAG, "Accessory opened");
        } else {
            setConnectionStatus(false);
            if (D)
                Log.d(TAG, "Accessory open failed");
        }
    }

    protected void setConnectionStatus(boolean connected) {
        Log.d(MainActivity.APP_CODE,"setConnectionStatus " + (connected ? "Connected" : "Disconnected") );
        listener.onChangeConnectionStatus(connected);
    }

    protected void closeAccessory() {
        setConnectionStatus(false);

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Close all streams
        try {
            if (mInputStream != null)
                mInputStream.close();
        } catch (Exception ignored) {
        } finally {
            mInputStream = null;
        }
        try {
            if (mOutputStream != null)
                mOutputStream.close();
        } catch (Exception ignored) {
        } finally {
            mOutputStream = null;
        }
        try {
            if (mFileDescriptor != null)
                mFileDescriptor.close();
        } catch (IOException ignored) {
        } finally {
            mFileDescriptor = null;
            mAccessory = null;
        }
    }



    protected class ConnectedThread extends Thread {
        Activity activity;
//        TextView mTextView;
        byte[] buffer = new byte[1024];
        boolean running;

        ConnectedThread(Activity activity) {
            this.activity = activity;
            running = true;
        }

        public void run() {
            while (running) {
                try {


                    final int bytes = mInputStream.read(buffer);
                    //listener.onDataReceived(buffer);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                long timer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
//                                mTextView.setText(Long.toString(timer));
                             listener.onDataReceived(buffer);
                        }
                    });
                } catch (final Exception ignore) {

                  /*  activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                long timer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
//                                mTextView.setText(Long.toString(timer));
                            Toast.makeText(BaseActivity.this, "Exception!"+ignore.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    });*/

                }
            }
        }

        public void cancel() {
            running = false;
        }
    }
    public void showDialog(Context ctx, String message, String okButton
            , DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(ctx)
                .setTitle("Arduino")
                .setMessage(message)
                .setPositiveButton(okButton, positiveListener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void connectToAccessory() {
        if (mAccessory != null) {
            setConnectionStatus(true);
            return;
        }

        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUsbManager.hasPermission(accessory))
                openAccessory(accessory);
            else {
                showDialog(this, "no permission", "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                setConnectionStatus(false);
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(accessory, mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            setConnectionStatus(false);
            if (D)
                Log.d(TAG, "mAccessory is null");
        }
    }



    public interface USBConnectListener{
        void onChangeConnectionStatus(boolean status);
        void onDataReceived(byte[] buffer);
    }

    public USBConnectListener getListener() {
        return listener;
    }

    public void setListener(USBConnectListener listener) {
        this.listener = listener;
    }
}
