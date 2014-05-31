package com.icecoreb.trainalert.activity;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.icecoreb.trainalert.R;
import com.icecoreb.trainalert.store.TrainAlertDataStore;

public class StoreTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_test);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_test, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		TextView textView = (TextView) this.findViewById(R.id.linesData);
		String fileContent = this.loadTrainLinesData();
		textView.setText(fileContent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String loadTrainLinesData() {
		List<JSONObject> trainLinesData = (new TrainAlertDataStore(this)).getTrainLines();
		return trainLinesData.isEmpty() ? "no data" : trainLinesData.get(0).toString();
	}

}
