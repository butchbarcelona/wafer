package com.proj.demo.wafer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.demo.BaseActivity;
import com.proj.demo.CountDownTimerThread;
import com.proj.wafer.R;

import java.util.concurrent.locks.Lock;


public class LockerFragment extends BaseFragment implements View.OnClickListener, BaseActivity.USBConnectListener {

    public int lockerTime1, lockerTime2, lockerTime3;
    SharedPreferences sharedPreferences;


    Button btnAdd, btnMinus;

    TextView tv1, tv2;

    TextView[] lockHour, lockMin, lockSec;

    int layoutId;

    int secInterval = 60;
    int pricePerInterval = 1;
    CountDownTimerThread cdt1, cdt2, cdt3;

    LinearLayout llLocker1, llLocker2, llLocker3;
    TextView tvLabel1, tvLabel2, tvLabel3;

    public LockerFragment() {
    }

    public LockerFragment setLayout(int layoutID) {
        layoutId = layoutID;

        if(LockerFragment.this.getActivity() != null) {
            switch (layoutID) {
                case R.layout.activtiy_claim:

                    llLocker1.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                    llLocker2.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                    llLocker3.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));

                    tvLabel1.setText("Input password and claim the baggage for this locker Number 1");
                    tvLabel2.setText("Input password and claim the baggage for this locker Number 2");
                    tvLabel3.setText("Input password and claim the baggage for this locker Number 3");

                    break;
                case R.layout.activity_locker:
                    llLocker1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    llLocker2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    llLocker3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

                    tvLabel1.setText("Input password for this locker Number 1");
                    tvLabel2.setText("Input password for this locker Number 2");
                    tvLabel3.setText("Input password for this locker Number 3");
                    break;
            }
        }

        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getApplicationContext().getSharedPreferences(MainActivity.APP_CODE, Context.MODE_MULTI_PROCESS);
        return init(inflater);
    }


    public View init(LayoutInflater inflater){

        View v = inflater.inflate(R.layout.activity_locker, null);
        llLocker1 = (LinearLayout) v.findViewById(R.id.ll_locker1);
        llLocker2 = (LinearLayout) v.findViewById(R.id.ll_locker2);
        llLocker3 = (LinearLayout) v.findViewById(R.id.ll_locker3);

        llLocker1.setOnClickListener(this);
        llLocker2.setOnClickListener(this);
        llLocker3.setOnClickListener(this);

        tvLabel1 = (TextView) v.findViewById(R.id.tv_label1);
        tvLabel2 = (TextView) v.findViewById(R.id.tv_label2);
        tvLabel3 = (TextView) v.findViewById(R.id.tv_label3);

        lockHour = new TextView[3];
        lockHour[0] = (TextView) v.findViewById(R.id.tv_hour1);
        lockHour[1] = (TextView) v.findViewById(R.id.tv_hour2);
        lockHour[2] = (TextView) v.findViewById(R.id.tv_hour3);

        lockMin = new TextView[3];
        lockMin[0] = (TextView) v.findViewById(R.id.tv_min1);
        lockMin[1] = (TextView) v.findViewById(R.id.tv_min2);
        lockMin[2] = (TextView) v.findViewById(R.id.tv_min3);

        lockSec = new TextView[3];
        lockSec[0] = (TextView) v.findViewById(R.id.tv_sec1);
        lockSec[1] = (TextView) v.findViewById(R.id.tv_sec2);
        lockSec[2] = (TextView) v.findViewById(R.id.tv_sec3);

        switch (layoutId) {
            case R.layout.activtiy_claim:

                llLocker1.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                llLocker2.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));
                llLocker3.setBackgroundColor(getResources().getColor(R.color.accent_material_dark));

                tvLabel1.setText("Input password and claim the baggage for this locker Number 1");
                tvLabel2.setText("Input password and claim the baggage for this locker Number 2");
                tvLabel3.setText("Input password and claim the baggage for this locker Number 3");

                break;
            case R.layout.activity_locker:
                llLocker1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                llLocker2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                llLocker3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

                tvLabel1.setText("Input password for this locker Number 1");
                tvLabel2.setText("Input password for this locker Number 2");
                tvLabel3.setText("Input password for this locker Number 3");
                break;
        }
        return v;
    }



    @Override
    public void onClick(View v) {
        boolean isUsed = false;
        switch(v.getId()){
            case R.id.ll_locker1:
                isUsed = sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER1, false);
                break;
            case R.id.ll_locker2:
                isUsed = sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER2, false);
                break;
            case R.id.ll_locker3:
                isUsed = sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER3, false);
                break;
        }

        if(isUsed){
            if(layoutId == R.layout.activtiy_claim){
                showDialogSetPassword(v.getId());
            }else{
                Toast.makeText(this.getActivity(),"This locker is being used.", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(layoutId == R.layout.activtiy_claim){
                Toast.makeText(this.getActivity(),"This locker is not being used.", Toast.LENGTH_SHORT).show();
            }else{
                showDialogSetPassword(v.getId());
            }
        }

    }




    public void showDialog(String message){
        new AlertDialog.Builder(this.getActivity())
            .setTitle(this.getActivity().getResources().getString(R.string.app_name))
            .setMessage(message)
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }


    public void showDialogPayPrice(final int id, final String idTime,final String idSetTime, final int total){

        final int credits = ((MainActivity)this.getActivity()).getCredits();
        final int price = ((total/secInterval)*pricePerInterval);
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_pay_price,null);


        TextView tvPrice, tvCreds;
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvPrice.setText(""+price);
        tvCreds = (TextView) view.findViewById(R.id.tv_credits);
        tvCreds.setText(""+((MainActivity)this.getActivity()).credits);

        new AlertDialog.Builder(this.getActivity())
            .setView(view)
            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                if(price <= credits) {
                    ((MainActivity) LockerFragment.this.getActivity()).setCredits(credits - price);
                    payedForTime(id, idTime, total, idSetTime);
                }else
                    showDialog("Insufficient balance.");
                }
            })
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();

    }


    public void payedForTime(int id, String idTime, int total, String idSetTime){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(idTime, total);
        editor.putInt(idSetTime, total);

        editor.commit();
        editor.apply();

        switch(id){
            case R.id.ll_locker1:
                ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("A");
                break;
            case R.id.ll_locker2:
                ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("B");
                break;
            case R.id.ll_locker3:
                ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("C");
                break;
        }

        createCountDownTimer(id, total);
    }

    public void showDialogSetPassword(final int id){

        Log.d(MainActivity.APP_CODE, "showDialogSetPassword" );
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_set_password,null);
        final EditText etPass = (EditText) view.findViewById(R.id.et_password);

        String title = "", idSPPass = "", idSPTime = "", idSPTimeLast;

        switch(id){
            case R.id.ll_locker1:
                title = "Locker Number 1";
                idSPPass = MainActivity.SP_PASS_LOCKER1;
                break;
            case R.id.ll_locker2:
                title = "Locker Number 2";
                idSPPass = MainActivity.SP_PASS_LOCKER2;
                break;
            case R.id.ll_locker3:
                title = "Locker Number 3";
                idSPPass = MainActivity.SP_PASS_LOCKER3;
                break;
        }

        if(layoutId == R.layout.activtiy_claim) {
            TextView tvMinRule = (TextView) view.findViewById(R.id.tv_min_rule);
            tvMinRule.setVisibility(View.GONE);
        }

        final String finalIdSPPass = idSPPass;
        new AlertDialog.Builder(this.getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String pass = etPass.getText().toString();
                    if(pass.length()>=6){

                        if(layoutId == R.layout.activity_locker) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(finalIdSPPass,pass);
                            editor.commit();
                            editor.apply();

                            showDialogSetPhoneNumber(id);
                        }else{
                            if(pass.equals(sharedPreferences.getString(finalIdSPPass,""))){

                               /* switch(id){
                                    case R.id.ll_locker1:
                                        if(cdt1 != null) cdt1.stopCount();

                                        break;
                                    case R.id.ll_locker2:
                                        if(cdt2 != null) cdt2.stopCount();
                                        break;
                                    case R.id.ll_locker3:
                                        if(cdt3 != null) cdt3.stopCount();

                                        break;
                                }*/
                                showDialogClaimTime(id);
                                dialog.dismiss();

                            }else{

                                Toast.makeText(LockerFragment.this.getActivity(),"Incorrect password.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(LockerFragment.this.getActivity(),"Please input a longer password.", Toast.LENGTH_LONG).show();
                    }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void showDialogClaimTime(final int id){

        Log.d(MainActivity.APP_CODE, "showDialogClaimTime" );
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_claim_time,null);

        TextView tvHour, tvMin, tvSec;
        tvHour = (TextView) view.findViewById(R.id.tv_hour);
        tvMin = (TextView) view.findViewById(R.id.tv_min);
        tvSec = (TextView) view.findViewById(R.id.tv_sec);


        String idTotTime = null;
        int totalSecTime = 0;
        int totalSetTime = 0;
        int totalTime = 0;
        switch(id){
            case R.id.ll_locker1:
                totalTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER1,0);
                totalSecTime = sharedPreferences.getInt(MainActivity.SP_TOTTIME_LOCKER1,0);
                totalSetTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER1_SET,0);
                break;
            case R.id.ll_locker2:
                totalTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER2,0);
                totalSecTime = sharedPreferences.getInt(MainActivity.SP_TOTTIME_LOCKER2,0);
                totalSetTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER2_SET,0);
                break;
            case R.id.ll_locker3:
                totalTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER3,0);
                totalSecTime = sharedPreferences.getInt(MainActivity.SP_TOTTIME_LOCKER3,0);
                totalSetTime = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER3_SET,0);
                break;
        }


        int secs = (totalTime< 10)?(totalSetTime-totalTime) :totalTime;//(int) (millisUntilFinished/1000);
        int s = secs%60;
        int m = (secs%3600 - s)/60;
        int h = secs/3600;

        int timeDiff = totalTime - totalSecTime;

        Log.d(MainActivity.APP_CODE, "timeDiff"+timeDiff);
        Log.d(MainActivity.APP_CODE, "totalTime"+totalTime);
        Log.d(MainActivity.APP_CODE, "totalSecTime"+totalSecTime);

        int unbilledTime = (totalTime >= 0)? 0:Math.abs(totalTime);
        //(totalTime < 10)?Math.abs(totalTime):0;//totalSecTime - totalSetTime;
        final int remainingTime = (unbilledTime/secInterval)*pricePerInterval;
                //((unbilledTime/(secInterval/60)) + (((unbilledTime%(secInterval/60)) > 0)?1:0)*pricePerInterval);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_credits);
        tvPrice.setText(remainingTime+"");

        //tvCreditsInner.setText(((MainActivity)this.getActivity()).credits+"");
/*
        if(((MainActivity)this.getActivity()).credits < remainingTime){
            tvPrice.setTextColor(Color.RED);
        }*/


        tvHour.setText(convertIntToString(h));
        tvMin.setText(convertIntToString(m));
        tvSec.setText(convertIntToString(s));

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(LockerFragment.this.getActivity());



            builder.setView(view)
            .setCancelable(false)
            .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(remainingTime > 0 ){

                    }else {

                        switch (id) {
                            case R.id.ll_locker1:
                                if (cdt1 != null) cdt1.stopCount();
                                editor.putInt(MainActivity.SP_TIME_LOCKER1, 0);
                                editor.putString(MainActivity.SP_PASS_LOCKER1, "");
                                editor.putInt(MainActivity.SP_TIME_LOCKER1_SET, 0);
                                editor.putInt(MainActivity.SP_TIME_LOCKER1_LAST, 0);
                                editor.putInt(MainActivity.SP_TOTTIME_LOCKER1, 0);
                                editor.putBoolean(MainActivity.SP_USED_LOCKER1, false);
                                editor.commit();
                                editor.apply();

                                lockHour[0].setText("00");
                                lockMin[0].setText("00");
                                lockSec[0].setText("00");

                                lockHour[0].setTextColor(Color.WHITE);
                                lockMin[0].setTextColor(Color.WHITE);
                                lockSec[0].setTextColor(Color.WHITE);

                                ((MainActivity) LockerFragment.this.getActivity()).sendMsgToArduino("A");

                                break;
                            case R.id.ll_locker2:
                                if (cdt2 != null) cdt2.stopCount();


                                editor.putInt(MainActivity.SP_TIME_LOCKER2, 0);
                                editor.putString(MainActivity.SP_PASS_LOCKER2, "");
                                editor.putInt(MainActivity.SP_TIME_LOCKER2_SET, 0);
                                editor.putInt(MainActivity.SP_TIME_LOCKER2_LAST, 0);
                                editor.putInt(MainActivity.SP_TOTTIME_LOCKER2, 0);
                                editor.putBoolean(MainActivity.SP_USED_LOCKER2, false);
                                editor.commit();
                                editor.apply();

                                lockHour[1].setText("00");
                                lockMin[1].setText("00");
                                lockSec[1].setText("00");
                                lockHour[1].setTextColor(Color.WHITE);
                                lockMin[1].setTextColor(Color.WHITE);
                                lockSec[1].setTextColor(Color.WHITE);

                                ((MainActivity) LockerFragment.this.getActivity()).sendMsgToArduino("B");
                                break;
                            case R.id.ll_locker3:
                                if (cdt3 != null) cdt3.stopCount();

                                editor.putInt(MainActivity.SP_TIME_LOCKER3, 0);
                                editor.putString(MainActivity.SP_PASS_LOCKER3, "");
                                editor.putInt(MainActivity.SP_TIME_LOCKER3_SET, 0);
                                editor.putInt(MainActivity.SP_TIME_LOCKER3_LAST, 0);
                                editor.putInt(MainActivity.SP_TOTTIME_LOCKER3, 0);
                                editor.putBoolean(MainActivity.SP_USED_LOCKER3, false);
                                editor.commit();
                                editor.apply();

                                lockHour[2].setText("00");
                                lockMin[2].setText("00");
                                lockSec[2].setText("00");

                                lockHour[2].setTextColor(Color.WHITE);
                                lockMin[2].setTextColor(Color.WHITE);
                                lockSec[2].setTextColor(Color.WHITE);

                                ((MainActivity) LockerFragment.this.getActivity()).sendMsgToArduino("C");
                                break;
                        }
                        dialog.dismiss();
                    }
                }
            })
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert);
            dialog = builder.create();
            dialog.show();

    }

    public void showDialogSetPhoneNumber(final int id){

        Log.d(MainActivity.APP_CODE, "showDialogSetPhoneNumber" );
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_set_password,null);
        final EditText etPass = (EditText) view.findViewById(R.id.et_password);
        etPass.setInputType(InputType.TYPE_CLASS_PHONE);
        TextView tvInstruct = (TextView) view.findViewById(R.id.tv_instruct);
        tvInstruct.setText("Please input your phone number:");
        TextView tvMinRule = (TextView) view.findViewById(R.id.tv_min_rule);
        tvMinRule.setVisibility(View.GONE);

        String title = "", idSPPass = "", idSPTime = "", idSPTimeLast, idSPPhoneNum = "", idSetTime = "";

        switch(id){
            case R.id.ll_locker1:
                title = "Locker Number 1";
                idSPPass = MainActivity.SP_PASS_LOCKER1;
                idSPTime = MainActivity.SP_TIME_LOCKER1;
                idSPTimeLast = MainActivity.SP_TIME_LOCKER1_LAST;
                idSPPhoneNum = MainActivity.SP_NUMBER_LOCKER1;
                idSetTime = MainActivity.SP_TIME_LOCKER1_SET;
                break;
            case R.id.ll_locker2:
                title = "Locker Number 2";
                idSPPass = MainActivity.SP_PASS_LOCKER2;
                idSPTime = MainActivity.SP_TIME_LOCKER2;
                idSPTimeLast = MainActivity.SP_TIME_LOCKER2_LAST;
                idSPPhoneNum = MainActivity.SP_NUMBER_LOCKER2;
                idSetTime = MainActivity.SP_TIME_LOCKER2_SET;
                break;
            case R.id.ll_locker3:
                title = "Locker Number 3";
                idSPPass = MainActivity.SP_PASS_LOCKER3;
                idSPTime = MainActivity.SP_TIME_LOCKER3;
                idSPTimeLast = MainActivity.SP_TIME_LOCKER3_LAST;
                idSPPhoneNum = MainActivity.SP_NUMBER_LOCKER3;
                idSetTime = MainActivity.SP_TIME_LOCKER3_SET;
                break;
        }


        final String finalIdSPPass = idSPPass;
        final String finalTitle = title;
        final String finalIdSPTime = idSPTime;
        final String finalIdSPPhoneNum = idSPPhoneNum;
        final String finalIdSetTime = idSetTime;
        new AlertDialog.Builder(this.getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = etPass.getText().toString();
                        if(!number.isEmpty()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(finalIdSPPhoneNum, number);
                            editor.commit();
                            editor.apply();
                            showDialogSetTime(finalTitle, finalIdSPTime, finalIdSetTime, id);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(LockerFragment.this.getActivity(),"Please input your phone number.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void showDialogSetTime(String title, final String idTime,final String idSetTime, final int id){

        Log.d(MainActivity.APP_CODE, "showDialogSetTime" );
        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_set_time,null);

        final EditText etHour, etMin, etSec;
        etHour = (EditText) view.findViewById(R.id.et_time_hour);
        etMin = (EditText) view.findViewById(R.id.et_time_min);
        etSec = (EditText) view.findViewById(R.id.et_time_sec);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnMinus = (Button) view.findViewById(R.id.btn_minus);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = ( Integer.parseInt(etHour.getText().toString())*3600)
                        + (Integer.parseInt(etMin.getText().toString())*60)
                        + Integer.parseInt(etSec.getText().toString());
                total += secInterval;

                int s = total%60;
                int m = (total%3600 - s)/60;
                int h = total/3600;
                etHour.setText(convertIntToString(h));
                etMin.setText(convertIntToString(m));
                etSec.setText(convertIntToString(s));

            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = ( Integer.parseInt(etHour.getText().toString())*3600)
                        + (Integer.parseInt(etMin.getText().toString())*60)
                        + Integer.parseInt(etSec.getText().toString());
                if(total > 0)
                    total -= secInterval;

                int s = total%60;
                int m = (total%3600 - s)/60;
                int h = total/3600;
                etHour.setText(convertIntToString(h));
                etMin.setText(convertIntToString(m));
                etSec.setText(convertIntToString(s));

            }
        });

        new AlertDialog.Builder(this.getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = Integer.parseInt(etHour.getText().toString());
                        int min = Integer.parseInt(etMin.getText().toString());
                        int sec = Integer.parseInt(etSec.getText().toString());
                        int total = (hour*3600) + (min*60) + sec;

                        showDialogPayPrice(id, idTime, idSetTime, total);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    public void createCountDownTimer(final int id, final int total){

        Log.d(MainActivity.APP_CODE, "createCountDownTimer total:"+total );

        CountDownTimer countDown = null;
        TextView tvHour = null, tvMin = null, tvSec = null;
        String idTime = null, idTimeLast = null, idPhoneNumber = null, idIsUsed = null, idTotTime= null, idSetTime = null;

        int lockerNum = 0;

        switch(id){
            case R.id.ll_locker1:
                lockerNum = 1;
                tvHour  = lockHour[0];
                tvMin  = lockMin[0];
                tvSec  = lockSec[0];
                idTime = MainActivity.SP_TIME_LOCKER1;
                idTimeLast = MainActivity.SP_TIME_LOCKER1_LAST;
                idPhoneNumber = MainActivity.SP_NUMBER_LOCKER1;
                idIsUsed = MainActivity.SP_USED_LOCKER1;
                idTotTime = MainActivity.SP_TOTTIME_LOCKER1;
                idSetTime = MainActivity.SP_TIME_LOCKER1_SET;
                break;
            case R.id.ll_locker2:
                lockerNum = 2;
                tvHour  = lockHour[1];
                tvMin  = lockMin[1];
                tvSec  = lockSec[1];
                idTime = MainActivity.SP_TIME_LOCKER2;
                idTimeLast = MainActivity.SP_TIME_LOCKER2_LAST;
                idPhoneNumber = MainActivity.SP_NUMBER_LOCKER2;
                idIsUsed = MainActivity.SP_USED_LOCKER2;
                idTotTime = MainActivity.SP_TOTTIME_LOCKER2;
                idSetTime = MainActivity.SP_TIME_LOCKER2_SET;
                break;
            case R.id.ll_locker3:
                lockerNum = 3;
                tvHour  = lockHour[2];
                tvMin  = lockMin[2];
                tvSec  = lockSec[2];
                idTime = MainActivity.SP_TIME_LOCKER3;
                idTimeLast = MainActivity.SP_TIME_LOCKER3_LAST;
                idPhoneNumber = MainActivity.SP_NUMBER_LOCKER3;
                idIsUsed = MainActivity.SP_USED_LOCKER3;
                idTotTime = MainActivity.SP_TOTTIME_LOCKER3;
                idSetTime = MainActivity.SP_TIME_LOCKER3_SET;
                break;
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(idIsUsed, true);
        //editor.putInt(idSetTime, total);
        editor.commit();
        editor.apply();

        final String phoneNumber = sharedPreferences.getString(idPhoneNumber,"");


        final TextView finalTvHour = tvHour;
        final TextView finalTvMin = tvMin;
        final TextView finalTvSec = tvSec;

        if(countDown != null){
            countDown.cancel();
        }

        final String finalIdTime = idTime;
        final String finalIdTimeLast = idTimeLast;


        final String finalIdTotTime = idTotTime;
        final String finalIdIsUsed = idIsUsed;
        final String finalIdSetTime = idSetTime;
        final int finalLockerNum = lockerNum;
        CountDownTimerThread cdt = new CountDownTimerThread(new CountDownTimerThread.CountDownListener() {
            @Override
            public void onTick(int currentCountdown) {
                Log.d(MainActivity.APP_CODE, "createCountDownTimer:onTick "+currentCountdown);
                if(currentCountdown < 0){
                    finalTvHour.setTextColor(Color.RED);
                    finalTvMin.setTextColor(Color.RED);
                    finalTvSec.setTextColor(Color.RED);
                }

                int secs = Math.abs(currentCountdown);//(int) (millisUntilFinished/1000);
                int s = secs%60;
                int m = (secs%3600 - s)/60;
                int h = secs/3600;

                finalTvHour.setText(convertIntToString(h));
                finalTvMin.setText(convertIntToString(m));
                finalTvSec.setText(convertIntToString(s));

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(finalIdTime, currentCountdown);
                editor.putInt(finalIdTimeLast, secs);
                editor.putInt(finalIdTotTime, sharedPreferences.getInt(finalIdSetTime, 0)+Math.abs(currentCountdown));
                editor.commit();
                editor.apply();

                if(currentCountdown >= 0) {
                    if (m == 10 && s == 0 && h == 0) {
                        sendSMS(LockerFragment.this.getActivity().getApplicationContext(), phoneNumber
                                , LockerFragment.this.getActivity().getResources()
                                        .getString(R.string.sms_10mins));

                        ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("!1"+phoneNumber);
                    } else if (m == 5 && s == 0 && h == 0) {
                        sendSMS(LockerFragment.this.getActivity().getApplicationContext(), phoneNumber
                                , LockerFragment.this.getActivity().getResources()
                                        .getString(R.string.sms_5mins));
                        ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("!2"+phoneNumber );
                    }else if (currentCountdown == 0){
                        sendSMS(LockerFragment.this.getActivity().getApplicationContext(),phoneNumber
                                ,LockerFragment.this.getActivity().getResources()
                                        .getString(R.string.sms_finished));

                        ((MainActivity)LockerFragment.this.getActivity()).sendMsgToArduino("!3"+phoneNumber);
                    }
                }


            }

            @Override
            public void onFinish() {
                Log.d(MainActivity.APP_CODE, "createCountDownTimer:onFinish");
                finalTvHour.setText("00");
                finalTvMin.setText("00");
                finalTvSec.setText("00");
                finalTvHour.setTextColor(Color.WHITE);
                finalTvMin.setTextColor(Color.WHITE);
                finalTvSec.setTextColor(Color.WHITE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(finalIdTime, 0);
                editor.putInt(finalIdTimeLast, 0);
                editor.putInt(finalIdTotTime, sharedPreferences.getInt(finalIdTotTime, 0)+1);
                editor.commit();
                editor.apply();

            }
        }, total);
        cdt.startCountdownTimer();

        switch(id){
            case R.id.ll_locker1:
                cdt1 = cdt;
                break;
            case R.id.ll_locker2:
                cdt2 = cdt;
                break;
            case R.id.ll_locker3:
                cdt3 = cdt;
                break;
        }


    }


    public static String convertIntToString(int num){
        if(num < 10){
            return "0"+String.valueOf(num);
        }else{
            return String.valueOf(num);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(MainActivity.APP_CODE,"onPause");
        if(cdt1 != null) cdt1.stopCount();
        if(cdt2 != null) cdt2.stopCount();
        if(cdt3 != null) cdt3.stopCount();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(MainActivity.APP_CODE,"onStop");
        if(cdt1 != null) cdt1.stopCount();
        if(cdt2 != null) cdt2.stopCount();
        if(cdt3 != null) cdt3.stopCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(MainActivity.APP_CODE,"onResume");

        lockerTime1 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER1,0);
        lockerTime2 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER2,0);
        lockerTime3 = sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER3,0);


        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime1);
        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime2);
        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime3);


        float lockerTime1Last = (float) sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER1_LAST,0) * 1.0F;
        float lockerTime2Last = (float) sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER2_LAST,0) * 1.0F;
        float lockerTime3Last = (float) sharedPreferences.getInt(MainActivity.SP_TIME_LOCKER3_LAST,0) * 1.0F;

        Log.d(MainActivity.APP_CODE, "lockerTime1Last: "+lockerTime1Last);
        Log.d(MainActivity.APP_CODE, "lockerTime2Last: "+lockerTime2Last);
        Log.d(MainActivity.APP_CODE, "lockerTime3Last: "+lockerTime3Last);


//        long currTime = System.currentTimeMillis();
//        lockerTime1 = Math.abs((int) (lockerTime1 - ((currTime - lockerTime1Last)/1000)));
//        lockerTime2 = Math.abs((int) (lockerTime2 - ((currTime - lockerTime2Last)/1000)));
//        lockerTime3 = Math.abs((int) (lockerTime3 - ((currTime - lockerTime3Last)/1000)));

        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime1);
        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime2);
        Log.d(MainActivity.APP_CODE, "onResume: "+lockerTime3);




        if(sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER1,false)){
            createCountDownTimer(R.id.ll_locker1, lockerTime1);
        }

        if(sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER2,false)){
            createCountDownTimer(R.id.ll_locker2, lockerTime2);
        }

        if(sharedPreferences.getBoolean(MainActivity.SP_USED_LOCKER3,false)){
            createCountDownTimer(R.id.ll_locker3, lockerTime3);
        }

    }


    @Override
    public void onChangeConnectionStatus(boolean status) {
        //tv2.setText(status?"connected":"disconnected");
    }

    @Override
    public void onDataReceived(byte[] buffer) {
        //long timer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
        //tv1.setText(Long.toString(timer));
/*        if(tvCreditsInner != null){

            int creds = ((MainActivity)this.getActivity()).credits;
            int n = buffer[0];
            if(n == 5){
                creds += 5;
                tvCreditsInner.setText(creds+"");
                ((MainActivity) LockerFragment.this.getActivity()).setCredits(creds);
            }
        }*/
    }
    public void sendSMS(final Context ctx, String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(ctx, 0,
                new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

    }



}
