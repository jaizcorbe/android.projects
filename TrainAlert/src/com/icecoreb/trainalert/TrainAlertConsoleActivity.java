package com.icecoreb.trainalert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class TrainAlertConsoleActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.train_alert_console, menu);
		return true;
	}

	// receiver for getting service checker updates
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			TrainAlertConsoleActivity.this.receiveUpdate(intent);
		}
	};

	// register and unregister receiver
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter iff = new IntentFilter();
		iff.addAction("com.icecoreb.trainalert.UPDATE");
		// Put whatever message you want to receive as the action
		this.registerReceiver(this.mBroadcastReceiver, iff);
	}

	@Override
	public void onPause() {
		super.onPause();
		this.unregisterReceiver(this.mBroadcastReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_alert_console);
		this.callCheckerService(CheckerCommand.CHECK_STATE);
	}

	protected void receiveUpdate(Intent intent) {
		String serviceState = intent.getExtras().getString(
				TrainCheckerService.SERVICE_STATE);
		int updateCount = intent.getExtras().getInt(
				TrainCheckerService.UPDATE_COUNT);
		String trainsSchedule = intent.getExtras().getString(
				TrainCheckerService.TRAIN_SCHEDULE);
		this.updateViews(serviceState, Integer.toString(updateCount),
				trainsSchedule);
	}

	private void updateViews(String serviceState, String updateCount,
			String trainsSchedule) {
		EditText stateText = (EditText) this.findViewById(R.id.serverStateText);
		stateText.setText("estado servicio: [" + serviceState + "]");
		EditText updateCountText = (EditText) this
				.findViewById(R.id.serverUpdateText);
		updateCountText.setText(updateCount);
		EditText trainsScheduleText = (EditText) this
				.findViewById(R.id.trainsScheduleText);
		trainsScheduleText.setText(trainsSchedule);
	}

	private void callCheckerService(CheckerCommand command) {
		Intent serviceStartIntent = new Intent(this, TrainCheckerService.class);
		serviceStartIntent.putExtra(TrainCheckerService.SERVICE_COMMAND,
				command.getValue());
		this.startService(serviceStartIntent);
	}

	public void startService(View view) {
		this.callCheckerService(CheckerCommand.START);
	}

	public void stopService(View view) {
		this.callCheckerService(CheckerCommand.STOP);
	}

}
