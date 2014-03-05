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
import com.icecoreb.trainalert.checking.AlertChecker;

/**
 * It is the service that checks the train schedule data and evaluates the
 * alerts on that data and throws the notification if it is required
 * 
 * This service is started and bound. This means that this service will run
 * until it is stopped but will rely on the binding mechanism to communicate
 * with its clients, for starting, stopping, defining its alerts or showing its
 * state The service is meant to run in the same thread as its clients, but the
 * work should be performed in another thread. The multi-threading is handled by
 * the TrainCheckerHandler which receives messages and executes them in the
 * thread to which it is associated to.
 * 
 * @author jaizcorbe
 * 
 */
// TODO investigate foreground service
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
	private final IBinder binder = new TrainCheckerServiceBinder();
	private ServiceState state;

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
		if (this.thread != null) {
			this.thread.quit();
		}
		Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return this.binder;
	}

	// TODO check flags and startId
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}

	private void sendMessage(int commandValue, int delay) {
		Message msg = this.handler.obtainMessage();
		msg.arg1 = commandValue;
		this.handler.sendMessageDelayed(msg, delay);
	}

	// --------------------------------------------------------------------/

	public synchronized ServiceState startService() {
		this.getState().setRunning(true);
		this.sendMessage(CheckerCommand.CHECK_TRAIN_SCHEDULE.getValue(), 0);
		return this.getState();
	}

	public synchronized ServiceState stopService() {
		this.getState().setRunning(false);
		this.getState().resetUpdateCount();
		this.getState().setTrainSchedule("No Trains Schedule available");
		this.handler.removeCallbacksAndMessages(null);
		return this.getState();
	}

	public synchronized void ckeckTrainSchedule() {
		this.getState().addUpdateCount();
		this.retrieveTrainsSchedule();
		this.sendMessage(CheckerCommand.CHECK_TRAIN_SCHEDULE.getValue(),
				UPDATE_RATE);
	}

	public synchronized void notifyStatus() {
		Intent updateIntent = new Intent();
		updateIntent.setAction("com.icecoreb.trainalert.UPDATE");
		this.sendBroadcast(updateIntent);
	}

	public synchronized ServiceState getState() {
		if (this.state == null) {
			this.state = ServiceState.getDefaultState();
		}
		return this.state;
	}

	// --------------------------------------------------------------------/

	private void retrieveTrainsSchedule() {
		AlertChecker checker = new AlertChecker();
		JSONObject stationSchedule = checker.checkAlert(this.getState()
				.getCurrentAlert(), this);
		this.getState().setTrainSchedule(stationSchedule.toString());
	}

}
