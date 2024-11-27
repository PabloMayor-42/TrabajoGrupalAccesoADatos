package com.retogrupal.entities;

import java.util.ArrayList;

public class RepresentacionTabla {
	//Cebecera con el nombre de las columnas de la tabla
	private ArrayList<String> encabezado;
	
	//Contenido del cuerpo del documento (por filas)
	private ArrayList<Residuo> cuerpo;
	
	public ArrayList<String> getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(ArrayList<String> encabezado) {
		this.encabezado = encabezado;
	}
	public ArrayList<Residuo> getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(ArrayList<Residuo> cuerpo) {
		this.cuerpo = cuerpo;
	}
	public RepresentacionTabla(ArrayList<String> encabezado, ArrayList<Residuo> cuerpo) {
		super();
		this.encabezado = encabezado;
		this.cuerpo = cuerpo;
	}
	public RepresentacionTabla() {

	}
	
}
