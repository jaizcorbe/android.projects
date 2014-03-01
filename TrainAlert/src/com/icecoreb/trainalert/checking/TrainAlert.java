package com.icecoreb.trainalert.checking;

import com.icecoreb.trainalert.model.Estacion;
import com.icecoreb.trainalert.model.Ramal;

public class TrainAlert {
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
}
