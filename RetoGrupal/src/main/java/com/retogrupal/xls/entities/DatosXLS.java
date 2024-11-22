package com.retogrupal.xls.entities;

import java.util.ArrayList;

public class DatosXLS {
	private String[] encabezado;
	private ArrayList<String[]> filas;
	public String[] getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(String[] encabezado) {
		this.encabezado = encabezado;
	}
	public ArrayList<String[]> getFilas() {
		return filas;
	}
	public void setFilas(ArrayList<String[]> filas) {
		this.filas = filas;
	}
	public DatosXLS(String[] encabezado, ArrayList<String[]> filas) {
		super();
		this.encabezado = encabezado;
		this.filas = filas;
	}
	public DatosXLS() {

	}
	
}
