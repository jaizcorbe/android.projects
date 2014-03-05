package com.icecoreb.trainalert.service;

import com.icecoreb.trainalert.checking.TrainAlert;

public class ServiceState {

	private TrainAlert currentAlert;
	private boolean running;
	private String trainSchedule;
	private String stationSchedule;
	private int updateCount = 0;

	public synchronized TrainAlert getCurrentAlert() {
		return currentAlert;
	}

	public synchronized void setCurrentAlert(TrainAlert currentAlert) {
		this.currentAlert = currentAlert;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}

	public synchronized String getRunningString() {
		return this.isRunning() ? "Running" : "Stopped";
	}

	public synchronized String getTrainSchedule() {
		return trainSchedule;
	}

	public synchronized void setTrainSchedule(String trainSchedule) {
		this.trainSchedule = trainSchedule;
	}

	public synchronized String getStationSchedule() {
		return stationSchedule;
	}

	public synchronized void setStationSchedule(String stationSchedule) {
		this.stationSchedule = stationSchedule;
	}

	public synchronized int getUpdateCount() {
		return updateCount;
	}

	public synchronized void resetUpdateCount() {
		this.updateCount = 0;
	}

	public synchronized void addUpdateCount() {
		this.updateCount++;
	}

	public static ServiceState getDefaultState() {
		ServiceState state = new ServiceState();
		state.setRunning(false);
		state.setCurrentAlert(TrainAlert.getDefaultAlert());
		state.setTrainSchedule("No Trains Schedule available");
		return state;
	}
}
