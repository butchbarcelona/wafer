package com.proj.demo.wafer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.proj.demo.BaseActivity;
import com.proj.wafer.R;

public class RateFragment extends BaseFragment implements BaseActivity.USBConnectListener {

    EditText etComment;
    RatingBar ratingBar;

    Button btnSubmit, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_rate, null);

        etComment = (EditText) v.findViewById(R.id.etComment);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

        btnSubmit = (Button) v.findViewById(R.id.btn_submit);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();

            }
        });

        return v;
    }



    @Override
    public void onChangeConnectionStatus(boolean status) {

    }

    @Override
    public void onDataReceived(byte[] buffer) {

    }



    public void resetFields(){
        etComment.setText("");
        ratingBar.setNumStars(0);
    }


}
