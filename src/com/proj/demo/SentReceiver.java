package com.proj.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * Created by mbarcelona on 12/12/15.
 */
public class SentReceiver extends BroadcastReceiver {

  public SentReceiver() {

  }

  @Override
  public void onReceive(Context context, Intent arg1) {
    switch (getResultCode()) {
      case Activity.RESULT_OK:
        //Util.getInstance().showSnackBarToast((Context) context, "SMS successfully sent.");
        break;
      case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
        //Util.getInstance().showSnackBarToast((Context) context, " SMS failed to send.");
        break;
      case SmsManager.RESULT_ERROR_NO_SERVICE:
        //Util.getInstance().showSnackBarToast((Context) context, "SMS failed to send. No service.");
        break;
/*      case SmsManager.RESULT_ERROR_NULL_PDU:
        Toast.makeText(context, "Null PDU",
            Toast.LENGTH_SHORT).show();
        break;
      case SmsManager.RESULT_ERROR_RADIO_OFF:
        Toast.makeText(context, "Radio off",
            Toast.LENGTH_SHORT).show();
        break;*/
    }


  }

}