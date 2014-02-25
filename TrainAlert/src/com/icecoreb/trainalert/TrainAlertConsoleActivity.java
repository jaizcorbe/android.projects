package com.icecoreb.trainalert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TrainAlertConsoleActivity extends Activity {

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			TrainAlertConsoleActivity.this.receiveUpdate(intent);
		}
	};

	private CheckerState state = CheckerState.stopped;

	protected void receiveUpdate(Intent intent) {
		String serviceStatus = intent.getExtras().getString(
				TrainCheckerService.SERVICE_STATE);
		EditText editText = (EditText) this.findViewById(R.id.dataDisplay);
		editText.setText("estado servicio: [" + serviceStatus + "]");
	}

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
		this.state = CheckerState.stopped;
		this.setButtonMsg();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.train_alert_console, menu);
		return true;
	}

	public void switchButtonClick(View view) {
		this.switchState();
		this.setButtonMsg();
		Intent serviceStartIntent = new Intent(this, TrainCheckerService.class);
		serviceStartIntent.putExtra(TrainCheckerService.SERVICE_COMMAND,
				this.getServiceCommand());
		this.startService(serviceStartIntent);
	}

	private String getServiceCommand() {
		if (CheckerState.stopped.equals(this.state)) {
			return TrainCheckerService.START;
		} else {
			return TrainCheckerService.STOP;
		}
	}

	private void switchState() {
		this.state = this.state.switchState();
	}

	private Button getButton() {
		Button button = (Button) this.findViewById(R.id.switchChecker);
		return button;
	}

	private void setButtonMsg() {
		Button button = this.getButton();
		button.setText(this.state.getStringMessageId());
	}

}
