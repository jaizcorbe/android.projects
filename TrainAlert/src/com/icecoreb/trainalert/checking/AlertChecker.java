package com.icecoreb.trainalert.checking;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.icecoreb.http.HttpReader;
import com.icecoreb.json.JsonBuilder;
import com.icecoreb.trainalert.R;

public class AlertChecker {
	private static final String DATA_URL = "http://trenes.mininterior.gov.ar/v2_pg/arribos/ajax_arribos.php?ramal=5&rnd=mLbJm8Z19IX7Zhka&key=v%23v%23QTUtWp%23MpWRy80Q0knTE10I30kj%23FNyZ";

	private HttpReader trainDataReader = new HttpReader();
	private JsonBuilder jsonBuilder = new JsonBuilder();

	public JSONObject checkAlert(TrainAlert alert, Context context) {
		JSONObject schedule = this.getStationSchedule(alert);
		try {
			String proximoStr = schedule.getString(TrainAlert.PROXIMO);
			int proximo = Integer.valueOf(proximoStr);
			if (0 <= proximo && proximo <= alert.getAlertMinutes()) {
				this.notifyAlert(context, alert);
			}
		} catch (Exception ex) {
			Log.e("AlertChecker", "Error al checkear la alerta", ex);
		}
		return schedule;
	}

	private void notifyAlert(Context context, TrainAlert alert) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Tren Llegando")
				.setContentText("a estacion: " + alert.getEstacion().toString())
		// Vibration
		.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
		// LED
		//mBuilder.setLights(Color.RED, 3000, 3000);
		// // Creates an explicit intent for an Activity in your app
		// Intent resultIntent = new Intent(this, ResultActivity.class);
		//
		// // The stack builder object will contain an artificial back stack for
		// the
		// // started Activity.
		// // This ensures that navigating backward from the Activity leads out
		// of
		// // your application to the Home screen.
		// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// // Adds the back stack for the Intent (but not the Intent itself)
		// stackBuilder.addParentStack(ResultActivity.class);
		// // Adds the Intent that starts the Activity to the top of the stack
		// stackBuilder.addNextIntent(resultIntent);
		// PendingIntent resultPendingIntent =
		// stackBuilder.getPendingIntent(
		// 0,
		// PendingIntent.FLAG_UPDATE_CURRENT
		// );
		// mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());
	}

	public JSONObject getStationSchedule(TrainAlert alert) {
		JSONArray data = this.getTrainData(alert);
		JSONObject stationData = alert.getEstacion().getEstacionJson(data);
		stationData = alert.getRamal().getDireccion()
				.getNextTrains(stationData);
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
