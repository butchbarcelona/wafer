package com.proj.demo.wafer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.proj.demo.BaseActivity;
import com.proj.wafer.R;


public class HowToUseFragment extends BaseFragment implements BaseActivity.USBConnectListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_how_to_use, null);
        return v;
    }



    @Override
    public void onChangeConnectionStatus(boolean status) {

    }

    @Override
    public void onDataReceived(byte[] buffer) {

    }
}
