package com.icecoreb.trainalert.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.service.ForegroundTrainCheckerService;
import com.icecoreb.trainalert.service.ServiceState;
import com.icecoreb.trainalert.service.TrainCheckerService;
import com.icecoreb.trainalert.service.TrainCheckerService.TrainCheckerServiceBinder;

public class TrainAlertConsoleActivity extends Activity {

	private TrainCheckerService service;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.train_alert_console, menu);
		return true;
	}

	// register and unregister receiver--------------------------------------

	// receiver for getting service checker updates
	// TODO check LocalBroadcastManager for sending broadcast intents within my
	// process
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			TrainAlertConsoleActivity.this.receiveUpdate(intent);
		}
	};

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

	// -----------------------------------------------------------------------/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_alert_console);
		this.startTrainCheckerService();
	}

	@Override
	protected void onDestroy() {
		this.stopTrainCheckerService();
		super.onDestroy();
	}

	private void startTrainCheckerService() {
		Intent serviceStartIntent = new Intent(this,
				ForegroundTrainCheckerService.class);
		this.startService(serviceStartIntent);
	}

	private void stopTrainCheckerService() {
		if (!this.getState().isRunning()) {
			Intent serviceStartIntent = new Intent(this,
					ForegroundTrainCheckerService.class);
			this.stopService(serviceStartIntent);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		// TODO check bind flags
		Intent bindIntent = new Intent(this, ForegroundTrainCheckerService.class);
		this.bindService(bindIntent, this.sConnection, Context.BIND_AUTO_CREATE);
		Toast.makeText(this, "service binded", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.unbindService(this.sConnection);
		Toast.makeText(this, "service unbinded", Toast.LENGTH_SHORT).show();
	}

	protected void receiveUpdate(Intent intent) {
		this.receiveUpdate(this.getState());
	}

	protected void receiveUpdate(ServiceState state) {
		String serviceState = state.getRunningString();
		int updateCount = state.getUpdateCount();
		String trainsSchedule = state.getTrainSchedule();
		this.updateViews(serviceState, Integer.toString(updateCount),
				trainsSchedule);
	}

	/* Called when the second activity's finished */
	// TODO check codes, result codes and others
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			this.receiveUpdate(this.getState());
		}
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
		EditText alertText = (EditText) this.findViewById(R.id.alertText);
		alertText.setText(this.getState().getCurrentAlert().toString());
	}

	// button click handlers-------------------------------------------------

	public void startService(View view) {
		this.service.startService();
		this.receiveUpdate(this.getState());
	}

	public void stopService(View view) {
		this.service.stopService();
		this.receiveUpdate(this.getState());
	}

	public void selectAlert(View view) {
		Intent intent = new Intent(this, AlertSettingActivity.class);
		this.startActivity(intent);
	}

	// service connection define the service binding calbacks------------------

	protected ServiceState getState() {
		if (this.service != null && this.service.getState() != null) {
			return this.service.getState();
		}
		return ServiceState.getDefaultState();
	}

	private ServiceConnection sConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,
				IBinder serviceBinder) {
			TrainCheckerServiceBinder binder = (TrainCheckerServiceBinder) serviceBinder;

			TrainAlertConsoleActivity.this.service = binder.getService();
			TrainAlertConsoleActivity.this
					.receiveUpdate(TrainAlertConsoleActivity.this.getState());
			if (TrainAlertConsoleActivity.this.service != null) {

				Toast.makeText(TrainAlertConsoleActivity.this,
						"service initialized in activity", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(TrainAlertConsoleActivity.this,
						"service initialized in activity as null",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			TrainAlertConsoleActivity.this.service = null;
		}
	};

}
