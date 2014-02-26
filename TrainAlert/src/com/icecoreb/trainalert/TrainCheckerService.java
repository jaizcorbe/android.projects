package com.icecoreb.trainalert;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class TrainCheckerService extends Service {

	public static final String SERVICE_STATE = "SERVICE_STATE";
	public static final String UPDATE_COUNT = "UPDATE COUNT";
	public static final String SERVICE_COMMAND = "SERVICE_COMMAND";
	public static final String START = "START";
	public static final String STOP = "STOP";
	public static final int UPDATE_RATE = 10000;

	private int updateCount = 0;
	private Handler handler;

	private CheckerState state;

	// runnables to execute actions
	Runnable start = new Runnable() {
		public void run() {
			if (CheckerState.started.equals(state)) {
				updateCount++;
				handler.postDelayed(start, UPDATE_RATE);
			}
			notifyStatus();
		}
	};

	@Override
	public void onCreate() {
		this.setState(CheckerState.stopped);
		this.handler = new Handler();
	}

	@Override
	public void onDestroy() {
		this.setState(CheckerState.stopped);
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String command = intent.getExtras().getString(SERVICE_COMMAND);
		if (command != null) {
			if (START.equals(command)) {
				this.startService();
			} else if (SERVICE_STATE.equals(command)) {
				this.notifyStatus();
			} else {
				this.stopService();
			}
		} else {
			this.stopService();
		}
		return START_NOT_STICKY;
	}

	private void startService() {
		if (this.state == null || CheckerState.stopped.equals(this.state)) {
			this.setState(CheckerState.started);
			this.handler.post(this.start);
		}
	}

	private void stopService() {
		this.setState(CheckerState.stopped);
	}

	private void notifyStatus() {
		Intent updateIntent = new Intent();
		updateIntent.setAction("com.icecoreb.trainalert.UPDATE");
		updateIntent.putExtra(SERVICE_STATE, this.state == null ? "NO STATE"
				: this.state.toString());
		updateIntent.putExtra(UPDATE_COUNT, this.updateCount);
		this.sendBroadcast(updateIntent);
	}

	private synchronized void setState(CheckerState state) {
		this.state = state;
	}
}
