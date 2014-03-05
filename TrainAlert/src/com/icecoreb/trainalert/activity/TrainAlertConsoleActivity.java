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

import com.icecoreb.trainalert.CheckerCommand;
import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.checking.TrainAlert;
import com.icecoreb.trainalert.service.TrainCheckerService;
import com.icecoreb.trainalert.service.TrainCheckerService.TrainCheckerServiceBinder;

public class TrainAlertConsoleActivity extends Activity {

	private TrainAlert alert = null;
	private TrainAlert defaultAlert = null;

	private TrainCheckerService service;

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

	// register and unregister receiver--------------------------------------
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

	// -----------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_train_alert_console);
		this.defaultAlert = TrainAlert.getDefaultAlert();
		this.callCheckerService(CheckerCommand.CHECK_STATE);
	}
	
	@Override
	protected void onStart () {
		super.onStart();
		//TODO check bind flags
		Intent bindIntent = new Intent(this, TrainCheckerService.class);
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
		String serviceState = intent.getExtras().getString(
				TrainCheckerService.SERVICE_STATE);
		int updateCount = intent.getExtras().getInt(
				TrainCheckerService.UPDATE_COUNT);
		String trainsSchedule = intent.getExtras().getString(
				TrainCheckerService.TRAIN_SCHEDULE);
		this.alert = TrainAlert.getTrainAlert(intent.getExtras());
		this.updateViews(serviceState, Integer.toString(updateCount),
				trainsSchedule);
	}

	/* Called when the second activity's finished */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			this.alert = TrainAlert.getTrainAlert(data.getExtras());
			this.updateAlertView();
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
		this.updateAlertView();
	}

	private void updateAlertView() {
		EditText alertText = (EditText) this.findViewById(R.id.alertText);
		alertText.setText(this.alert == null ? this.defaultAlert.toString()
				: this.alert.toString());
	}

	private void callCheckerService(CheckerCommand command) {
		Intent serviceStartIntent = new Intent(this, TrainCheckerService.class);
		serviceStartIntent.putExtra(TrainCheckerService.SERVICE_COMMAND,
				command.getValue());
		if (this.alert != null) {
			serviceStartIntent.putExtras(this.alert.getBundledData());
		}
		this.startService(serviceStartIntent);
	}

	public void startService(View view) {
		this.callCheckerService(CheckerCommand.START);
	}

	public void stopService(View view) {
		this.callCheckerService(CheckerCommand.STOP);
	}

	public void selectAlert(View view) {
		Intent intent = new Intent(this, AlertSettingActivity.class);
		this.startActivityForResult(intent, 0);
	}

	// service connection define the service binding calbacks------------------

	private ServiceConnection sConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className,
				IBinder serviceBinder) {
			TrainCheckerServiceBinder binder = (TrainCheckerServiceBinder) serviceBinder;
			TrainAlertConsoleActivity.this.service = binder.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			TrainAlertConsoleActivity.this.service = null;
		}
	};

}
