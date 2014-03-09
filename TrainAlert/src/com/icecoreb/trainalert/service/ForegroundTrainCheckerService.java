package com.icecoreb.trainalert.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.activity.TrainAlertConsoleActivity;

public class ForegroundTrainCheckerService extends TrainCheckerService {

	final static int ONGOING_NOTIFICATION_ID = 1234;

	@Override
	public synchronized ServiceState startService() {
		ServiceState state = super.startService();
		Notification notification = this.getForegroundNotification();
		this.startForeground(ONGOING_NOTIFICATION_ID, notification);
		return state;
	}

	@Override
	public synchronized ServiceState stopService() {
		ServiceState state = super.stopService();
		this.stopForeground(true);
		return state;
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
		notice.setLatestEventInfo(this, "Checking Trains", "The service is still running",
				pendIntent);

		notice.flags |= Notification.FLAG_NO_CLEAR;
		return notice;
	}
}
