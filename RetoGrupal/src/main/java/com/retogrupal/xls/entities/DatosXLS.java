package com.retogrupal.xls.entities;

import java.util.ArrayList;

public class DatosXLS {
	private String[] encabezado;
	private ArrayList<Object[]> cuerpo;
	public String[] getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(String[] encabezado) {
		this.encabezado = encabezado;
	}
	public ArrayList<Object[]> getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(ArrayList<Object[]> cuerpo) {
		this.cuerpo = cuerpo;
	}
	public DatosXLS(String[] encabezado, ArrayList<Object[]> cuerpo) {
		super();
		this.encabezado = encabezado;
		this.cuerpo = cuerpo;
	}
	public DatosXLS() {

	}
	
}
