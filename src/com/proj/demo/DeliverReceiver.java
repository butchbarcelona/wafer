package com.proj.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mbarcelona on 12/12/15.
 */
public class DeliverReceiver extends BroadcastReceiver {

  public DeliverReceiver() {

  }

  @Override
  public void onReceive(Context context, Intent arg1) {
    switch (getResultCode()) {
      case Activity.RESULT_OK:
        Toast.makeText(context, "SMS delivered",
            Toast.LENGTH_SHORT).show();
        break;
      case Activity.RESULT_CANCELED:
        Toast.makeText(context, "SMS not delivered",
            Toast.LENGTH_SHORT).show();
        break;
    }

  }
}