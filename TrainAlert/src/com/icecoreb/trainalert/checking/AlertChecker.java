package com.icecoreb.trainalert.checking;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.icecoreb.http.HttpReader;
import com.icecoreb.json.JsonBuilder;

public class AlertChecker {
	private static final String DATA_URL = "http://trenes.mininterior.gov.ar/v2_pg/arribos/ajax_arribos.php?ramal=5&rnd=mLbJm8Z19IX7Zhka&key=v%23v%23QTUtWp%23MpWRy80Q0knTE10I30kj%23FNyZ";

	private HttpReader trainDataReader = new HttpReader();
	private JsonBuilder jsonBuilder = new JsonBuilder();

	public void checkAlert(TrainAlert alert) {

	}

	public JSONObject getStationSchedule(TrainAlert alert) {
		JSONArray data = this.getTrainData(alert);
		JSONObject stationData = alert.getEstacion().getEstacionJson(data);
		return stationData;
	}

	private JSONArray getTrainData(TrainAlert alert) {

		try {
			String data = this.trainDataReader.readUrl(DATA_URL);
			JSONArray jsonData = this.jsonBuilder.getJason(data);
			return jsonData;
		} catch (Exception ex) {
			Log.e("ALERT CHECKING", "error tying to read schedule data", ex);
			return new JSONArray();
		}

	}
}
