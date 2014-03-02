package com.icecoreb.trainalert.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.checking.TrainAlert;
import com.icecoreb.trainalert.model.Estacion;
import com.icecoreb.trainalert.model.Ramal;

public class AlertSettingActivity extends ListActivity {

	private TrainAlert[] alerts = null;
	private String[] alertViews = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_alert_setting);
		// Show the Up button in the action bar.
		// setupActionBar();
		this.initializeAlertsListView();
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
		Intent resultIntent = new Intent();
		resultIntent.putExtras(alert.getBundledData());
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
