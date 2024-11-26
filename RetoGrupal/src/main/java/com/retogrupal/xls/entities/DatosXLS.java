package com.retogrupal.xls.entities;

import java.util.ArrayList;

public class DatosXLS {
	//Cebecera con el nombre de las columnas de la tabla
	private String[] encabezado;
	
	//Contenido del cuerpo del documento (por filas)
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
