package com.icecoreb.trainalert.model;

import org.json.JSONArray;
import org.json.JSONObject;

public enum Estacion {
	retiro("Retiro"), lisandro_de_la_torre("Lisandro de la Torre"), belgrano_c(
			"Belgrano C"), nunez("Nu�ez"), rivadavia("Rivadavia"), vicente_lopez(
			"Vicente L�pez"), olivos("Olivos"), la_lucila("La Lucila"), martinez(
			"Mart�nez"), acassuso("Acassuso"), san_isidro_c("San Isidro C"), beccar(
			"B�ccar"), victoria("Victoria"), virreyes("Virreyes"), san_fernando_c(
			"San Fernando C"), carupa("Carup�"), tigre("Tigre");

	private String nombre;

	private Estacion(String nombre) {
		this.nombre = nombre;
	}

	public JSONObject getEstacionJson(JSONArray data) {
		try {
			for (int i = 0; i < data.length(); i++) {
				JSONObject current = (JSONObject) data.get(i);
				if (this.nombre.equals(current.get("nombre"))) {
					return current;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
