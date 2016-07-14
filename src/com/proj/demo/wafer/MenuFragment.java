package com.proj.demo.wafer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.proj.demo.BaseActivity;
import com.proj.wafer.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Created by mbarcelona on 7/2/16.
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener,  BaseActivity.USBConnectListener{


    SharedPreferences sharedPreferences;
    ImageButton btnRate, btnUse, btnContact, btnDeposit, btnClaim;

    public float userCredits;
    public int lockerTime1, lockerTime2, lockerTime3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_menu, null);

        btnRate = (ImageButton)v.findViewById(R.id.btn_rate);
        btnUse = (ImageButton)v.findViewById(R.id.btn_use);
        btnContact = (ImageButton)v.findViewById(R.id.btn_contact);
        btnDeposit = (ImageButton)v.findViewById(R.id.btn_deposit);
        btnClaim = (ImageButton)v.findViewById(R.id.btn_claim);

        btnRate.setOnClickListener(this);
        btnUse.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnDeposit.setOnClickListener(this);
        btnClaim.setOnClickListener(this);


        sharedPreferences = this.getActivity().getApplicationContext().getSharedPreferences(MainActivity.APP_CODE, Context.MODE_MULTI_PROCESS);
        userCredits = sharedPreferences.getFloat(MainActivity.SP_CREDITS,0f);

        lockerTime1 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER1,0);
        lockerTime2 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER2,0);
        lockerTime3 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER3,0);


        return v;
    }

    @Override
    public void onClick(View v) {
/*
        Intent intent = new Intent(MainActivity.this, LockerFragment.class);

        switch(v.getId()){
            case R.id.btn_claim:
                intent = new Intent(MainActivity.this, LockerFragment.class);
                break;
            case R.id.btn_deposit:
                intent = new Intent(MainActivity.this, LockerFragment.class);
                break;
            case R.id.btn_use:
                intent = new Intent(MainActivity.this, HowToUseFragment.class);
                break;
            case R.id.btn_contact:
                intent = new Intent(MainActivity.this, ContactInformationFragment.class);
                break;
            case R.id.btn_rate:
                intent = new Intent(MainActivity.this, RateFragment.class);
                break;
        }

        intent.putExtra(SP_CREDITS, userCredits);
        intent.putExtra(SP_TIME_LOCKER1, lockerTime1);
        intent.putExtra(SP_TIME_LOCKER2, lockerTime2);
        intent.putExtra(SP_TIME_LOCKER3, lockerTime3);

        startActivity(intent);*/
        switch(v.getId()){
            case R.id.btn_claim:

                ((MainActivity)this.getActivity()).changeFragment(R.layout.activtiy_claim);
                break;
            case R.id.btn_deposit:

                ((MainActivity)this.getActivity()).changeFragment(R.layout.activity_locker);
                break;
            case R.id.btn_use:

                //((MainActivity)MenuFragment.this.getActivity()).sendMsgToArduino("A");
                ((MainActivity)this.getActivity()).changeFragment(R.layout.activity_how_to_use);
                break;
            case R.id.btn_contact:

                //((MainActivity)MenuFragment.this.getActivity()).sendMsgToArduino("!109182839981");
                ((MainActivity)this.getActivity()).changeFragment(R.layout.activity_contact_information);
                break;
            case R.id.btn_rate:
                ((MainActivity)this.getActivity()).changeFragment(R.layout.activity_rate);
                break;
        }


    }




    @Override
    public void onChangeConnectionStatus(boolean status) {
        //btnRate.setText(status?"connected":"disconnected");
    }


    @Override
    public void onDataReceived(byte[] buffer) {
  /*      long timer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
        btnContact.setText(Long.toString(timer));*/
//        int nMsg = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt(); //.asCharBuffer().toString();
//        Log.d(MainActivity.APP_CODE, nMsg + "");
//        btnContact.setText(nMsg + "");
    }
}


