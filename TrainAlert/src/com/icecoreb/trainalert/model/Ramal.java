package com.icecoreb.trainalert.model;


public enum Ramal {
	//linea sarmiento
	//ramal moreno
//linea mitre
	//ramal tigre, mitre, j.l.suarez
//linea roca
	//ramal la plata
	mitre_tigre_a_tigre(Linea.mitre, Direccion.ida),
	mitre_tigre_a_retiro(Linea.mitre, Direccion.vuelta),
	mitre_mitre_a_mitre(Linea.mitre, Direccion.ida),
	mitre_mitre_a_retiro(Linea.mitre, Direccion.vuelta),
	mitre_j_l_suarez_a_suarez(Linea.mitre, Direccion.ida),
	mitre_j_l_suarez_a_retiro(Linea.mitre, Direccion.vuelta);
	
	private Linea linea;
	private Direccion direccion;
	
	private Ramal(Linea linea, Direccion direccion) {
		this.setLinea(linea);
		this.setDireccion(direccion);
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
}
