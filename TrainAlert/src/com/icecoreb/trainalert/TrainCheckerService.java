package com.icecoreb.trainalert;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TrainCheckerService extends Service {

	public static final String SERVICE_STATE = "SERVICE_STATE";
	public static final String SERVICE_COMMAND = "SERVICE_COMMAND";
	public static final String START = "START";
	public static final String STOP = "STOP";

	private int status = 10;
	private Handler handler;
	private boolean running;

	Runnable run = new Runnable() {
		public void run() {
			notifyStatus();
			if (running) {
				handler.postDelayed(run, 1000);
			}
		}
	};

	Runnable stop = new Runnable() {
		public void run() {
			setState(false);
		}
	};

	@Override
	public void onCreate() {
		this.running = false;
		this.handler = new Handler();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.notifyStatus();
		String command = intent.getExtras().getString(SERVICE_COMMAND);
		if(command != null && START.equals(command)) {
			this.setState(true);
			this.handler.post(this.run);
		} else {
			this.handler.post(this.stop);
		}
		return START_NOT_STICKY;
	}

	private void notifyStatus() {
		Intent updateIntent = new Intent();
		updateIntent.setAction("com.icecoreb.trainalert.UPDATE");
		updateIntent.putExtra(SERVICE_STATE,
				(Integer.valueOf(this.status++)).toString());
		this.sendBroadcast(updateIntent);
	}

	private synchronized void setState(boolean state) {
		this.running = state;
	}
}
