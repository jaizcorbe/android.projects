package com.icecoreb.trainalert.model;

import org.json.JSONObject;

import com.icecoreb.trainalert.checking.TrainAlert;

public enum Direccion {

	ida {
		public JSONObject getNextTrains(JSONObject stationInfo) {
			try {
				String nombre = stationInfo.getString("nombre");
				String proximo = stationInfo.getString("minutos_1");
				JSONObject rta = new JSONObject();
				rta.put(TrainAlert.ESTACION, nombre);
				rta.put(TrainAlert.PROXIMO, proximo);
				return rta;
			} catch (Exception e) {
				return null;
			}
		}
	},
	vuelta {
		public JSONObject getNextTrains(JSONObject stationInfo) {
			try {
				String nombre = stationInfo.getString("nombre");
				String proximo = stationInfo.getString("minutos_3");
				JSONObject rta = new JSONObject();
				rta.put(TrainAlert.ESTACION, nombre);
				rta.put(TrainAlert.PROXIMO, proximo);
				return rta;
			} catch (Exception e) {
				return null;
			}
		}
	};

	public abstract JSONObject getNextTrains(JSONObject stationInfo);
}

// minutos_1: "12",
// minutos_2: "30",
// minutos_3: "5",
// minutos_4: "24"