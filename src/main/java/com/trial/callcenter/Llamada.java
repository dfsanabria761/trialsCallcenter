package com.trial.callcenter;

public class Llamada {
	
	private final static int MIN_DURACION = 5;
	
	private final static int MAX_DURACION = 10;
	
	
	private double duracion;
	
	public Llamada() {
		int rango = MAX_DURACION-MIN_DURACION;
		this.duracion = (Math.random()*rango)+MIN_DURACION;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}
	
	
}
