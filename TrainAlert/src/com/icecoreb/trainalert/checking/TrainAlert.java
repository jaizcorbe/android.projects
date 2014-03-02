package com.icecoreb.trainalert.checking;

import com.icecoreb.trainalert.model.Estacion;
import com.icecoreb.trainalert.model.Ramal;

public class TrainAlert {

	public static final String TRAIN_ALERT_RAMAL = "TRAIN_ALERT_RAMAL";
	public static final String TRAIN_ALERT_ESTACION = "TRAIN_ALERT_ESTACION";
	public static final String TRAIN_ALERT_MINUTES = "TRAIN_ALERT_MINUTES";

	private Estacion estacion;
	private Ramal ramal;
	private int alertMinutes;

	public TrainAlert(Estacion estacion, Ramal ramal, int alertMinutes) {
		this.estacion = estacion;
		this.ramal = ramal;
		this.alertMinutes = alertMinutes;
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
