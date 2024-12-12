package com.retogrupal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.retogrupal.enitites.Residuo;
import com.retogrupal.utils.RepresentacionTabla;
import com.retogrupal.utils.UtilidadXML;
import com.retogrupal.utils.UtilidadesJSON;
import com.retogrupal.utils.UtilidadesXLS;

/**
 * Servlet implementation class ServletFichero
 */
@WebServlet("/ServletFichero")
public class ServletFichero extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletFichero() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Fichero al que se accedera segun lo seleccionado
		String despachar = null;

		// fichero que se desea leer/escribir
		String fichero = request.getParameter("fichero");

		// Segun sea Leer o escribir se hara una cosa u otra
		switch (request.getParameter("accion")) {
		case "lectura":
			// Leemos el tipo de fichero seleccionado
			ArrayList<Residuo> residuos = new ArrayList();
			switch (fichero) {
			case "CSV":
				// Creamos un CSVReader para leer el fichero CSV
				// Con getRealPath seleccionamos la ruta del fichero
				CSVReader reader = new CSVReader(new FileReader(
						getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv")));
				String[] nextLine;
				DateTimeFormatter frmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
				int i = 0;
				try {
					// Leemos las lineas del csv individualmente
					while ((nextLine = reader.readNext()) != null) {
						if (i > 0) {

							// Accede a los valores individuales
							LocalDate fecha = LocalDateTime.parse(nextLine[0], frmt).toLocalDate();
							String tipoResiduo = nextLine[1];
							String modalidad = nextLine[2];
							Double cantidad = Double.parseDouble(nextLine[3].replace(",", "."));

							// Creamos un nuevo Residuo
							Residuo dato = new Residuo(fecha, tipoResiduo, modalidad, cantidad);
							// Añadimos el residuo a la lista
							residuos.add(dato);

						} else {
							// La primera linea es el encabezado por lo que lo guardamos aparte
							String fecha = nextLine[0];
							String tipoResiduo = nextLine[1];
							String modalidad = nextLine[2];
							String cantidad = nextLine[3];

							ArrayList<String> encabezado = new ArrayList<>();
							encabezado.add(fecha);
							encabezado.add(tipoResiduo);
							encabezado.add(modalidad);
							encabezado.add(cantidad);

							request.setAttribute("encabezado", encabezado);

						}
						i++;
					}

				} catch (CsvValidationException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reader.close();
				break;
			case "XLS":
				RepresentacionTabla tabla = UtilidadesXLS
						.leer(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xls"));
				if (tabla != null) {
					request.setAttribute("encabezado", tabla.getEncabezado());
					residuos = tabla.getCuerpo();
				}
				break;
			case "ODS":

				break;
			case "JSON":
				RepresentacionTabla tablaJ = UtilidadesJSON.leer(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.json"));
				ArrayList<String> a = new ArrayList<>();
				a.add("a");
				a.add("a");
				a.add("a");
				a.add("a");
				if (tablaJ != null) {
					request.setAttribute("encabezado", a);
					residuos = tablaJ.getCuerpo();
				}
				break;
			case "XML":
				try {
					residuos = UtilidadXML.LeerXML(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xml"));
					request.setAttribute("encabezado", UtilidadXML.CargarEncabezados());
				} catch (ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					request.setAttribute("error", e.getMessage());
				}
				break;
			}
			
			
			request.setAttribute("residuos", residuos);
			despachar = "AccesoDatosA.jsp";
			break;
		case "escritura":
			String fecha = request.getParameter("dato1");
			String tipoResiduo = request.getParameter("dato2");
			String modalidad = request.getParameter("dato3");
			String cantidad = request.getParameter("dato4");

			if (fecha.isBlank() || tipoResiduo.isBlank() || modalidad.isBlank() || cantidad.isBlank()) {
				String error = "";
				if (fecha.isBlank()) {
					error += "No se ha introducido Dato 1\n";
				}
				if (tipoResiduo.isBlank()) {
					error += "No se ha introducido Dato 2\n";
				}
				if (modalidad.isBlank()) {
					error += "No se ha introducido Dato 3\n";
				}
				if (cantidad.isBlank()) {
					error += "No se ha introducido Dato 4\n";
				}
				despachar = "Error.jsp";

			} else {
				try {

					switch (fichero) {
					case "CSV":
						String path = getServletContext()
								.getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv");
						System.out.println(path);

						CSVWriter writer = new CSVWriter(new FileWriter(path, true));

						String[] datos = { (fecha + "T00:00," + tipoResiduo + "," + modalidad + "," + cantidad) };
						writer.writeNext(datos);
						writer.close();
						despachar = "TratamientoFich.jsp";

						break;
					case "XLS":
						boolean estadoXLS = UtilidadesXLS.escribir(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xls"),
								new Residuo(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
										tipoResiduo, modalidad, Double.parseDouble(cantidad)));
						if (!estadoXLS) {
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero XLS");
						} else {
							despachar = "TratamientoFich.jsp";
						}
						break;
					case "ODS":

						break;
					case "JSON":
						boolean estadoJSON = UtilidadesJSON.escribir(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.json"),
								new Residuo(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
										tipoResiduo, modalidad, Double.parseDouble(cantidad)));
						if (!estadoJSON) {
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero JSON");
						} else {
							despachar = "TratamientoFich.jsp";
						}
						break;
					case "XML":
						try {
							Residuo r = new Residuo(
									LocalDate.parse(fecha,DateTimeFormatter.ofPattern("yyyy-MM-dd")),
									tipoResiduo , modalidad,  
									Double.parseDouble(cantidad));
							UtilidadXML.EscribirXML(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xml"), r);
							despachar = "TratamientoFich.jsp";
						} catch (ParserConfigurationException | SAXException | IOException
								| TransformerFactoryConfigurationError | TransformerException e) {
							// TODO Auto-generated catch block
							request.setAttribute("error", e.getMessage());
						}
						break;
					}
				} catch (Exception e) {
					String error = e.getMessage();
					request.setAttribute("error", error);
					despachar = "Error.jsp";
				}
			}
			break;
		}

		if (despachar == null) {
			despachar = "Error.jsp";
		}

		request.getRequestDispatcher(despachar).forward(request, response);
	}

}
