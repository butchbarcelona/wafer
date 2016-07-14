package com.proj.demo.wafer;

import android.support.v4.app.Fragment;
import com.proj.demo.BaseActivity;

/**
 * Created by mbarcelona on 7/2/16.
 */
public abstract class BaseFragment extends Fragment implements BaseActivity.USBConnectListener {

    public BaseActivity.USBConnectListener getListener(){
        return this;
    }



}
