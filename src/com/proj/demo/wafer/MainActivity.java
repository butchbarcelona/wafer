package com.proj.demo.wafer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.*;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.demo.BaseActivity;
import com.proj.demo.DeliverReceiver;
import com.proj.demo.SentReceiver;
import com.proj.wafer.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends BaseActivity implements BaseActivity.USBConnectListener{

    public static final String APP_CODE = "COBLocker";

    public static final String SP_CREDITS = "credits";

    public static final String SP_NUMBER_LOCKER1 = "number_locker1";
    public static final String SP_NUMBER_LOCKER2 = "number_locker2";
    public static final String SP_NUMBER_LOCKER3 = "number_locker3";

    public static final String SP_TIME_LOCKER1 = "time_locker1";
    public static final String SP_TIME_LOCKER2 = "time_locker2";
    public static final String SP_TIME_LOCKER3 = "time_locker3";

    public static final String SP_TIME_LOCKER1_SET = "set_time_locker1";
    public static final String SP_TIME_LOCKER2_SET = "set_time_locker2";
    public static final String SP_TIME_LOCKER3_SET = "set_time_locker3";

    public static final String SP_PASS_LOCKER1 = "pass_locker1";
    public static final String SP_PASS_LOCKER2 = "pass_locker2";
    public static final String SP_PASS_LOCKER3 = "pass_locker3";

    public static final String SP_TIME_LOCKER1_LAST = "lasttime_locker1";
    public static final String SP_TIME_LOCKER2_LAST = "lasttime_locker2";
    public static final String SP_TIME_LOCKER3_LAST = "lasttime_locker3";

    public static final String SP_USED_LOCKER1 = "used_locker1";
    public static final String SP_USED_LOCKER2 = "used_locker2";
    public static final String SP_USED_LOCKER3 = "used_locker3";


    public static final String SP_TOTTIME_LOCKER1 = "total_locker1";
    public static final String SP_TOTTIME_LOCKER2 = "total_locker2";
    public static final String SP_TOTTIME_LOCKER3 = "total_locker3";

    public static SentReceiver sentReceiver = null;
    public static DeliverReceiver deliverReceiver = null;

    int credits = 0;

    BaseFragment currFrag;
    TextView tvCredits;

    ImageView imgConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        /*getActionBar().hide();*/
        // get an instance of FragmentTransaction from your Activity
        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        if (savedInstanceState == null) {
            currFrag = new MenuFragment();
            fragmentTransaction.add(R.id.frame_content, currFrag);
            fragmentTransaction.commit();
        }

        tvCredits = (TextView) findViewById(R.id.tv_credits);
        tvCredits.setText(" "+credits);

        imgConnection = (ImageView) findViewById(R.id.img_connection);
        this.setListener(this);

    }



    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
        tvCredits.setText(" "+credits);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSMSReceivers();

    }

    LockerFragment lockerFragment;
    MenuFragment menuFragment;
    ContactInformationFragment contactInformationFragment;
    HowToUseFragment howToUseFragment;
    RateFragment rateFragment;

    public void changeFragment(int layout){

        FragmentManager fragmentManager =
                this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currFrag);

        switch(layout){
            case R.layout.activity_main_menu:
                if(menuFragment == null) {
                    menuFragment = new MenuFragment();
                    fragmentTransaction.add(R.id.frame_content, menuFragment);
                }
                currFrag = menuFragment;
                break;
            case R.layout.activity_contact_information:
                if(contactInformationFragment == null) {
                    contactInformationFragment = new ContactInformationFragment();
                    fragmentTransaction.add(R.id.frame_content, contactInformationFragment);
                }
                currFrag = contactInformationFragment;

                break;
            case R.layout.activity_how_to_use:

                if(howToUseFragment == null) {
                    howToUseFragment = new HowToUseFragment();
                    fragmentTransaction.add(R.id.frame_content, howToUseFragment);
                }
                currFrag = howToUseFragment;
                break;
            case R.layout.activity_locker:
                if(lockerFragment == null) {
                    lockerFragment = new LockerFragment().setLayout(R.layout.activity_locker);
                    currFrag = lockerFragment;
                    fragmentTransaction.add(R.id.frame_content, currFrag);
                }else{
                    currFrag = lockerFragment.setLayout(R.layout.activity_locker);
                }
                break;
            case R.layout.activtiy_claim:
                if(lockerFragment == null) {
                    lockerFragment = new LockerFragment().setLayout(R.layout.activtiy_claim);
                    currFrag = lockerFragment;
                    fragmentTransaction.add(R.id.frame_content, currFrag);
                }else{
                    currFrag = lockerFragment.setLayout(R.layout.activtiy_claim);
                }
                break;
            case R.layout.activity_rate:
                if(rateFragment == null) {
                    rateFragment = new RateFragment();
                    fragmentTransaction.add(R.id.frame_content, rateFragment);
                }
                currFrag = rateFragment;
                break;
        }

        fragmentTransaction.show(currFrag);
        fragmentTransaction.commit();
    }


    @Override
    public void onChangeConnectionStatus(boolean status) {

        imgConnection.setBackgroundResource(status? R.drawable.green_circle: R.drawable.red_circle);
        if(currFrag != null && currFrag.isVisible())
            currFrag.getListener().onChangeConnectionStatus(status);
    }

    @Override
    public void onDataReceived(byte[] buffer) {

/*
        int nMsg = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt(); //.asCharBuffer().toString();
        Log.d(MainActivity.APP_CODE, nMsg + "");
*/
        int nMsg = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
        int n = nMsg;//buffer[0];


        Toast.makeText(this,",message:"+n+"",Toast.LENGTH_SHORT).show();

        if(n == 555){
            credits += 5;
            tvCredits.setText(credits+"");
        }


        if(currFrag.isVisible())
            currFrag.getListener().onDataReceived(buffer);
    }

    @Override
    public void onBackPressed() {
        changeFragment(R.layout.activity_main_menu);
    }


    public void registerSMSReceivers() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        sentReceiver = new SentReceiver();
        deliverReceiver = new DeliverReceiver();
        registerReceiver( MainActivity.sentReceiver, new IntentFilter(SENT));
        registerReceiver( MainActivity.deliverReceiver, new IntentFilter(DELIVERED));
    }

    public void unregisterSMSReceivers() {
        if (sentReceiver != null) {
            unregisterReceiver(sentReceiver);
            sentReceiver = null;
        }
        if (deliverReceiver != null) {
            unregisterReceiver(deliverReceiver);
            deliverReceiver = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSMSReceivers();
    }

    public void sendMsgToArduino(String message) {
        Log.d(MainActivity.APP_CODE, "sendMsgToArduino: message="+message );
        Toast.makeText(this,"Sending message to arduino : "+message,Toast.LENGTH_SHORT);
        //byte buffer = (byte) ((((ToggleButton) v).isChecked()) ? 1 : 0); // Read button

        byte[] buffer = message.getBytes();

        if (mOutputStream != null) {
            try {
                //for(int i = 0; i < buffer.length; i++)
                {
                    mOutputStream.write(buffer);//[i]);
                }
            } catch (IOException e) {
                if (D)
                    Log.e(MainActivity.APP_CODE, "write failed", e);
            }
        }
    }


}
