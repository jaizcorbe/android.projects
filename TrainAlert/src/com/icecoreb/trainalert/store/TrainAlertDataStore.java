package com.icecoreb.trainalert.store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.icecoreb.trainalert.R;

public class TrainAlertDataStore {

	private final String ALERTS_FILE = "trainAlerts.json";

	private Context context;

	public TrainAlertDataStore(Context context) {
		this.context = context;
	}

	public List<JSONObject> getTrainLines() {

		String data = this.readTrainLinesData();
		try {
			return this.retrieveJSONList(data);
		} catch (JSONException e) {
			return Collections.emptyList();
		}
	}

	public List<JSONObject> getTrainAlerts() {
		String data = this.readTrainAlertsData();
		try {
			return this.retrieveJSONList(data);
		} catch (JSONException e) {
			return Collections.emptyList();
		}
	}

	public void storeTrainAlert(JSONObject alertData) {
		try {
			if (alertData != null) {
				OutputStream os = this.context.openFileOutput(ALERTS_FILE,
						Context.MODE_PRIVATE);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						os));
				bw.write(alertData.toString());
				bw.flush();
				bw.close();
			}
		} catch (FileNotFoundException e) {
			// TODO something
		} catch (IOException e) {
			// TODO something
		}

	}

	private String readTrainLinesData() {
		InputStream linesInputStream = this.context.getResources()
				.openRawResource(R.raw.lines);
		return this.read(linesInputStream);
	}

	private String readTrainAlertsData() {
		try {
			InputStream is = this.context.openFileInput(ALERTS_FILE);
			return this.read(is);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private String read(InputStream is) {
		StringBuffer sb = new StringBuffer();
		BufferedReader sr = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while ((line = sr.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			sb.append(e.getMessage());
		}

		return sb.toString();
	}

	private List<JSONObject> retrieveJSONList(String data) throws JSONException {
		List<JSONObject> lines = new ArrayList<JSONObject>();
		if (data != null) {
			JSONTokener parser = new JSONTokener(data);
			JSONObject currentElement = null;
			while (parser.more()) {
				currentElement = (JSONObject) parser.nextValue();
				lines.add(currentElement);
			}
		}
		return lines;
	}
}
