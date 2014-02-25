package com.icecoreb.trainalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TrainCheckUpdateReceiver extends BroadcastReceiver {
	   @Override
	   public void onReceive(Context context, Intent intent) {
	      Toast.makeText(context, "Receiver Executed", Toast.LENGTH_LONG).show();
	   }
}