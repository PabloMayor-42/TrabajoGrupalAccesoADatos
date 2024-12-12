package com.retogrupal.enitites;

import java.time.LocalDate;

public class Residuo {
	
	private LocalDate mes;
	private String residuo;
	private String modalidad;
	private Double cantidad;
	
	public Residuo(LocalDate mes, String residuo, String modalidad, Double cantidad) {
		super();
		this.mes = mes;
		this.residuo = residuo;
		this.modalidad = modalidad;
		this.cantidad = cantidad;
	}
	
	public Residuo(LocalDate fecha, String residuo2, String modalidad2, String cantidad2) {
		// TODO Auto-generated constructor stub
		super();
		this.mes = mes;
		this.residuo = residuo;
		this.modalidad = modalidad;
		this.cantidad = cantidad;
	}

	public LocalDate getMes() {
		return mes;
	}
	public void setMes(LocalDate mes) {
		this.mes = mes;
	}
	public String getResiduo() {
		return residuo;
	}
	public void setResiduo(String residuo) {
		this.residuo = residuo;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
