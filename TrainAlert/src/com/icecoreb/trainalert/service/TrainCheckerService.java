package com.icecoreb.trainalert.service;

import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.icecoreb.trainalert.CheckerCommand;
import com.icecoreb.trainalert.CheckerState;
import com.icecoreb.trainalert.checking.AlertChecker;
import com.icecoreb.trainalert.checking.TrainAlert;

public class TrainCheckerService extends Service {

	public static final String TRAIN_SCHEDULE = "TRAIN_SCHEDULE_DATA";
	public static final String SERVICE_STATE = "SERVICE_STATE_DATA";
	public static final String UPDATE_COUNT = "UPDATE COUNT_DATA";
	public static final String SERVICE_COMMAND = "SERVICE_COMMAND";
	public static final int UPDATE_RATE = 60000;

	// multithreading support
	private Looper looper;
	private TrainCheckerHandler handler;
	private HandlerThread thread;
	private int updateCount = 0;
	private String trainsSchedule;
	private CheckerState state;
	private TrainAlert alert;

	public class TrainCheckerServiceBinder extends Binder {
		public TrainCheckerService getService() {
			return TrainCheckerService.this;
		}
	}

	// Handler that receives messages from the thread
	private final class TrainCheckerHandler extends Handler {
		public TrainCheckerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			int commandValue = msg.arg1;
			CheckerCommand command = CheckerCommand.fromValue(commandValue);
			synchronized (this) {
				command.executeCommand(TrainCheckerService.this);
			}
		}
	}

	@Override
	public void onCreate() {
		this.setState(CheckerState.stopped);
		// this.handler = new Handler();
		this.thread = new HandlerThread("ServiceStartArguments",
				android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		// Get the HandlerThread's Looper and use it for our Handler
		this.looper = thread.getLooper();
		if (this.looper != null) {
			this.handler = new TrainCheckerHandler(this.looper);
			Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show();
		} else {
			Log.e("Train Schedule Checker", "Error thread not initialized");
			this.stopSelf();
		}

	}

	@Override
	public void onDestroy() {
		this.setState(CheckerState.stopped);
		if (this.thread != null) {
			this.thread.quit();
		}
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.setTrainAlert(intent);
		int commandValue = intent.getExtras().getInt(SERVICE_COMMAND);
		this.sendMessage(commandValue, 0);
		return START_NOT_STICKY;
	}

	private void setTrainAlert(Intent intent) {
		TrainAlert alert = TrainAlert.getTrainAlert(intent.getExtras());
		if (alert != null) {
			this.alert = alert;
		}
	}

	private void sendMessage(int commandValue, int delay) {
		Message msg = this.handler.obtainMessage();
		msg.arg1 = commandValue;
		this.handler.sendMessageDelayed(msg, delay);
	}

	// command handling methods------------------------------------------
	public void startService() {
		if (this.state == null || CheckerState.stopped.equals(this.state)) {
			this.setState(CheckerState.started);
		}
	}

	public void ckeckTrainSchedule() {
		if (CheckerState.started.equals(state)) {
			updateCount++;
			this.retrieveTrainsSchedule();
			this.sendMessage(CheckerCommand.CHECK_TRAIN_SCHEDULE.getValue(),
					UPDATE_RATE);
			this.notifyStatus();
		} else {
			this.notifyStatus();
			this.stopSelf();
		}
	}

	public void stopService() {
		this.trainsSchedule = "No Trains Schedule available";
		this.setState(CheckerState.stopped);
	}

	public void notifyStatus() {
		Intent updateIntent = new Intent();
		updateIntent.setAction("com.icecoreb.trainalert.UPDATE");
		updateIntent.putExtra(SERVICE_STATE, this.state == null ? "NO STATE"
				: this.state.toString());
		updateIntent.putExtra(UPDATE_COUNT, this.updateCount);

		String scheduleContent;
		if (this.trainsSchedule != null) {
			scheduleContent = this.trainsSchedule;
		} else {
			scheduleContent = "No Trains Schedule available";
		}
		updateIntent.putExtra(TRAIN_SCHEDULE, scheduleContent);
		if (this.alert != null) {
			updateIntent.putExtras(this.alert.getBundledData());
		}
		this.sendBroadcast(updateIntent);
	}

	private synchronized void setState(CheckerState state) {
		this.state = state;
	}

	// --------------------------------------------------------------------

	private void retrieveTrainsSchedule() {
		// TrainAlert alert = new TrainAlert(Estacion.belgrano_c,
		// Ramal.mitre_tigre_a_tigre, 10);
		AlertChecker checker = new AlertChecker();
		JSONObject stationSchedule = checker.checkAlert(this.getCurrentAlert(),
				this);
		this.trainsSchedule = stationSchedule.toString();
		// try {
		// String content = reader
		// .readUrl("http://trenes.mininterior.gov.ar/v2_pg/arribos/ajax_arribos.php?ramal=5&rnd=mLbJm8Z19IX7Zhka&key=v%23v%23QTUtWp%23MpWRy80Q0knTE10I30kj%23FNyZ");
		// this.trainsSchedule = content;
		// } catch (Exception e) {
		// this.trainsSchedule = "error trying to retrieve trains shedule";
		// this.stopService();
		// }
	}

	private TrainAlert getCurrentAlert() {
		if (this.alert != null) {
			return this.alert;
		}
		return TrainAlert.getDefaultAlert();
	}
}
