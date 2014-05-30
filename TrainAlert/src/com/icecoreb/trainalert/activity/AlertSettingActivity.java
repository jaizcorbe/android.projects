package com.icecoreb.trainalert.activity;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.checking.TrainAlert;
import com.icecoreb.trainalert.model.Estacion;
import com.icecoreb.trainalert.model.Ramal;
import com.icecoreb.trainalert.service.ForegroundTrainCheckerService;
import com.icecoreb.trainalert.service.ServiceState;
import com.icecoreb.trainalert.service.TrainCheckerService;
import com.icecoreb.trainalert.service.TrainCheckerService.TrainCheckerServiceBinder;

public class AlertSettingActivity extends ListActivity {

	private TrainAlert[] alerts = null;
	private String[] alertViews = null;

	private TrainCheckerService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_alert_setting);
		// Show the Up button in the action bar.
		// setupActionBar();
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_alert_setting);
		this.initializeAlertsListView();
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

	private void initializeAlertsListView() {
		this.setAlerts();
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.alertViews));
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				// Toast.makeText(AlertSettingActivity.this,
				// AlertSettingActivity.this.alerts[position].toString(),
				// Toast.LENGTH_SHORT).show();
				AlertSettingActivity.this
						.finishWithResult(AlertSettingActivity.this.alerts[position]);
			}
		});
	}

	private void setAlerts() {
		this.alerts = new TrainAlert[4];
		this.alerts[0] = new TrainAlert(Estacion.belgrano_c,
				Ramal.mitre_tigre_a_tigre, 10);
		this.alerts[1] = new TrainAlert(Estacion.belgrano_c,
				Ramal.mitre_tigre_a_retiro, 10);
		this.alerts[2] = new TrainAlert(Estacion.san_isidro_c,
				Ramal.mitre_tigre_a_tigre, 10);
		this.alerts[3] = new TrainAlert(Estacion.san_isidro_c,
				Ramal.mitre_tigre_a_retiro, 10);
		this.alertViews = new String[4];
		this.alertViews[0] = alerts[0].toString();
		this.alertViews[1] = alerts[1].toString();
		this.alertViews[2] = alerts[2].toString();
		this.alertViews[3] = alerts[3].toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert_setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void finishWithResult(TrainAlert alert) {
		// Intent resultIntent = new Intent();
		// resultIntent.putExtras(alert.getBundledData());
		// setResult(RESULT_OK, resultIntent);
		this.getState().setCurrentAlert(alert);
		finish();
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

			AlertSettingActivity.this.service = binder.getService();
			// AlertSettingActivity.this
			// .receiveUpdate(AlertSettingActivity.this.getState());
			if (AlertSettingActivity.this.service != null) {

				Toast.makeText(AlertSettingActivity.this,
						"service initialized in alert setting activity",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						AlertSettingActivity.this,
						"service initialized in alert setting activity as null",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			AlertSettingActivity.this.service = null;
		}
	};

}
