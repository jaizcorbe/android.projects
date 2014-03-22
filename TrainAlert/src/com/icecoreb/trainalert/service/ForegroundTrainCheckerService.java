package com.icecoreb.trainalert.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.activity.TrainAlertConsoleActivity;

public class ForegroundTrainCheckerService extends TrainCheckerService {

	final static int ONGOING_NOTIFICATION_ID = 1234;

	private PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate() {
		super.onCreate();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		//TODO modify tag value
		this.wakeLock = pm
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
	}

	@Override
	public void onDestroy() {
		this.releaseWakeLock();
		super.onDestroy();
	}

	@Override
	public synchronized ServiceState startService() {
		this.aquireWakeLock();
		ServiceState state = super.startService();
		Notification notification = this.getForegroundNotification();
		this.startForeground(ONGOING_NOTIFICATION_ID, notification);
		return state;
	}

	@Override
	public synchronized ServiceState stopService() {
		ServiceState state = super.stopService();
		this.stopForeground(true);
		this.releaseWakeLock();
		return state;
	}

	private void aquireWakeLock() {
		if (this.wakeLock != null && !this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (this.wakeLock != null && this.wakeLock.isHeld()) {
			this.wakeLock.release();
		}
	}

	// TODO check no deprecated notification handling
	private Notification getForegroundNotification() {
		// The intent to launch when the user clicks the expanded notification
		Intent intent = new Intent(this, TrainAlertConsoleActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendIntent = PendingIntent
				.getActivity(this, 0, intent, 0);

		// This constructor is deprecated. Use Notification.Builder instead
		Notification notice = new Notification(R.drawable.ic_launcher,
				"Checking Trains", System.currentTimeMillis());

		// This method is deprecated. Use Notification.Builder instead.
		notice.setLatestEventInfo(this, "Checking Trains",
				"The service is still running", pendIntent);

		notice.flags |= Notification.FLAG_NO_CLEAR;
		return notice;
	}
}
