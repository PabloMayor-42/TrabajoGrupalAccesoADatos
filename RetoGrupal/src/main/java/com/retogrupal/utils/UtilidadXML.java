package com.retogrupal.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.retogrupal.enitites.Residuo;

public class UtilidadXML {

	public static ArrayList<Residuo> LeerXML(String ruta) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<Residuo> residuos = new ArrayList<Residuo>();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File archivo = new File(ruta);
		Document documento = builder.parse(archivo);
		Element incidencia = documento.getDocumentElement();
		NodeList lista_nodos = incidencia.getElementsByTagName("row");
		for(int i = 0;i<lista_nodos.getLength();i++) {
			Node nodo = lista_nodos.item(i);
			if(nodo.getNodeType() == Node.ELEMENT_NODE) {
				Element residuo = (Element) nodo;
				LocalDate fecha = LocalDate.parse(residuo.getAttribute("Mes").substring(0, 10),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				Residuo r = new Residuo(fecha, residuo.getAttribute("Residuo"), residuo.getAttribute("Modalidad"), 
						Double.parseDouble(residuo.getAttribute("Cantidad").replace(',', '.')));
				residuos.add(r);
			}
		}
		return residuos;
	}
	
	public static void EscribirXML(String ruta,Residuo residuo) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File archivo = new File(ruta);
		Document documento = builder.parse(archivo);
		Element incidencia = documento.getDocumentElement();
		Element nuevo = documento.createElement("row");
		nuevo.setAttribute("Mes", residuo.getMes().toString()+"T00:00");
		nuevo.setAttribute("Residuo", residuo.getResiduo());
		nuevo.setAttribute("Modalidad", residuo.getModalidad());
		nuevo.setAttribute("Cantidad", residuo.getCantidad().toString().replace('.', ','));
		incidencia.appendChild(nuevo);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(documento),new StreamResult(new File(ruta)));
	}
	
	public static ArrayList<String> CargarEncabezados(){
		ArrayList<String> encabezados = new ArrayList<String>();
		encabezados.add("Mes");
		encabezados.add("Residuo");
		encabezados.add("Modalidad");
		encabezados.add("Cantidad");
		return encabezados;
	}
	
	
}
