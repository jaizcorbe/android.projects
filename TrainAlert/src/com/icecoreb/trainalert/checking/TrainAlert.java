package com.icecoreb.trainalert.checking;

import android.os.Bundle;

import com.icecoreb.trainalert.model.Estacion;
import com.icecoreb.trainalert.model.Ramal;

public class TrainAlert {

	public static final String TRAIN_ALERT_RAMAL = "TRAIN_ALERT_RAMAL";
	public static final String TRAIN_ALERT_ESTACION = "TRAIN_ALERT_ESTACION";
	public static final String TRAIN_ALERT_MINUTES = "TRAIN_ALERT_MINUTES";
	
	public static final String ESTACION = "Estacion";
	public static final String PROXIMO = "Proximo";

	private Estacion estacion;
	private Ramal ramal;
	private int alertMinutes;

	public TrainAlert(Estacion estacion, Ramal ramal, int alertMinutes) {
		this.estacion = estacion;
		this.ramal = ramal;
		this.alertMinutes = alertMinutes;
	}

	public TrainAlert(String estacion, String ramal, int alertMinutes) {
		this.estacion = Estacion.valueOf(estacion);
		this.ramal = Ramal.valueOf(ramal);
		this.alertMinutes = alertMinutes;
	}

	public static TrainAlert getTrainAlert(Bundle bundle) {
		String estacion = bundle.getString(TRAIN_ALERT_ESTACION);
		String ramal = bundle.getString(TRAIN_ALERT_RAMAL);
		int minutes = bundle.getInt(TRAIN_ALERT_MINUTES);
		if (estacion != null && ramal != null) {
			return new TrainAlert(estacion, ramal, minutes);
		}
		return null;
	}

	public static TrainAlert getDefaultAlert() {
		TrainAlert alert = new TrainAlert(Estacion.belgrano_c,
				Ramal.mitre_tigre_a_tigre, 10);
		return alert;
	}

	public Bundle getBundledData() {
		Bundle data = new Bundle();
		data.putString(TRAIN_ALERT_ESTACION, this.estacion.toString());
		data.putString(TRAIN_ALERT_RAMAL, this.ramal.toString());
		data.putInt(TRAIN_ALERT_MINUTES, this.alertMinutes);
		return data;
	}

	public Estacion getEstacion() {
		return estacion;
	}

	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}

	public Ramal getRamal() {
		return ramal;
	}

	public void setRamal(Ramal ramal) {
		this.ramal = ramal;
	}

	public int getAlertMinutes() {
		return alertMinutes;
	}

	public void setAlertMinutes(int alertMinutes) {
		this.alertMinutes = alertMinutes;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(this.ramal.toString());
		str.append("\n");
		str.append(this.estacion.toString());
		str.append("\n");
		str.append(this.alertMinutes);
		str.append(" mins");
		return str.toString();
	}
}
