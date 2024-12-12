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
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;
import org.xml.sax.SAXException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.retogrupal.enitites.Residuo;
import com.retogrupal.utils.RepresentacionTabla;
import com.retogrupal.utils.UtilidadesXML;
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
		switch (request.getAttribute("accion") != null ? (String) request.getAttribute("accion")
				: request.getParameter("accion")) {
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
				RepresentacionTabla tablaXLS = UtilidadesXLS
						.leer(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xls"));
				if (tablaXLS != null) {
					request.setAttribute("encabezado", tablaXLS.getEncabezado());
					residuos = tablaXLS.getCuerpo();
				}
				break;
			case "ODS":
				File f = new File(
						getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.ods"));
				SpreadsheetDocument document = null;
				try {
					document = SpreadsheetDocument.loadDocument(f);
					Table hoja = document.getSheetByIndex(0);

					// Leemos el encabezado
					String fechaEnc = hoja.getCellByPosition(0, 0).getStringValue();
					String tipoResiduoEnc = hoja.getCellByPosition(1, 0).getStringValue();
					String modalidadEnc = hoja.getCellByPosition(2, 0).getStringValue();
					String cantidadEnc = hoja.getCellByPosition(3, 0).getStringValue();

					ArrayList<String> encabezado = new ArrayList<>();
					encabezado.add(fechaEnc);
					encabezado.add(tipoResiduoEnc);
					encabezado.add(modalidadEnc);
					encabezado.add(cantidadEnc);

					request.setAttribute("encabezado", encabezado);

					DateTimeFormatter frmt2 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

					for (int row = 1; row < hoja.getRowCount(); row++) {
						String fechaStr = hoja.getCellByPosition(0, row).getStringValue();
						if (fechaStr == null || fechaStr.isEmpty())
							break;

						LocalDate fecha = LocalDateTime.parse(fechaStr, frmt2).toLocalDate();
						String tipoResiduo = hoja.getCellByPosition(1, row).getStringValue();
						String modalidad = hoja.getCellByPosition(2, row).getStringValue();
						Double cantidad = Double
								.parseDouble(hoja.getCellByPosition(3, row).getStringValue().replace(",", "."));

						// Creamos un nuevo Residuo
						Residuo dato = new Residuo(fecha, tipoResiduo, modalidad, cantidad);
						// Añadimos el residuo a la lista
						residuos.add(dato);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (document != null) {
						try {
							document.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case "JSON":
				RepresentacionTabla tablaJSON = UtilidadesJSON
						.leer(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.json"));
				if (tablaJSON != null) {
					request.setAttribute("encabezado", tablaJSON.getEncabezado());
					residuos = tablaJSON.getCuerpo();
				}
				break;
			case "XML":
				try {
					residuos = UtilidadesXML.LeerXML(
							getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xml"));
					request.setAttribute("encabezado", UtilidadesXML.CargarEncabezados());
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
					error += "No se ha introducido 'Fecha'<br>";
				}
				if (tipoResiduo.isBlank()) {
					error += "No se ha introducido 'Residuo'<br>";
				}
				if (modalidad.isBlank()) {
					error += "No se ha introducido 'Modalidad'<br>";
				}
				if (cantidad.isBlank()) {
					error += "No se ha introducido 'Cantidad'";
				}

				request.setAttribute("error", error);
				despachar = "Error.jsp";

			} else {
				try {

					switch (fichero) {
					case "CSV":
						String path = getServletContext()
								.getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv");

						try {
							CSVWriter writer = new CSVWriter(new FileWriter(path, true));

							String[] datos = { (fecha + "T00:00"), tipoResiduo, modalidad, cantidad + "" };
							writer.writeNext(datos);

							writer.close();

							// Cargar lectura
							request.setAttribute("accion", "lectura");
							doPost(request, response);
							return; // Evitar despachar 2 veces
						} catch (Exception e) {
							e.printStackTrace();
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero ODS");
						}

						break;
					case "XLS":
						boolean estadoXLS = UtilidadesXLS.escribir(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xls"),
								new Residuo(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
										tipoResiduo, modalidad, Double.parseDouble(cantidad)));
						if (!estadoXLS) {
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero XLS");
						} else {
							// Cargar lectura
							request.setAttribute("accion", "lectura");
							doPost(request, response);
							return; // Evitar despachar 2 veces
						}
						break;
					case "ODS":
						File f = new File(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.ods"));
						SpreadsheetDocument document = null;
						try {
							document = SpreadsheetDocument.loadDocument(f);
							// Obtener la primera hoja por índice
							Table hoja = document.getSheetByIndex(0);

							int nextFila = 1;

							for (int row = 1; row < hoja.getRowCount(); row++) {
								String dato = hoja.getCellByPosition(0, row).getStringValue();
								if (dato == null || dato.isEmpty()) {
									nextFila = row;
									break; // Sale del bucle una vez encontrada la fila vacía
								}
							}

							// Escribir en la siguiente fila
							hoja.getCellByPosition(0, nextFila).setStringValue(fecha + "T00:00");
							hoja.getCellByPosition(1, nextFila).setStringValue(tipoResiduo);
							hoja.getCellByPosition(2, nextFila).setStringValue(modalidad);
							hoja.getCellByPosition(3, nextFila)
									.setDoubleValue(Double.parseDouble(cantidad.replace(",", ".")));

							// Guardar los cambios en el mismo archivo
							document.save(f);

							// Cargar lectura
							request.setAttribute("accion", "lectura");
							doPost(request, response);
							return; // Evitar despachar 2 veces
						} catch (Exception e) {
							e.printStackTrace();
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero ODS");
						} finally {
							if (document != null) {
								try {
									document.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						break;
					case "JSON":
						boolean estadoJSON = UtilidadesJSON.escribir(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.json"),
								new Residuo(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
										tipoResiduo, modalidad, Double.parseDouble(cantidad)));
						if (!estadoJSON) {
							request.setAttribute("error", "Error al realizar la escritura sobre el fichero JSON");
						} else {
							// Cargar lectura
							request.setAttribute("accion", "lectura");
							doPost(request, response);
							return; // Evitar despachar 2 veces
						}
						break;
					case "XML":
						try {
							Residuo r = new Residuo(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
									tipoResiduo, modalidad, Double.parseDouble(cantidad));
							UtilidadesXML.EscribirXML(getServletContext()
									.getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.xml"), r);

							// Cargar lectura
							request.setAttribute("accion", "lectura");
							doPost(request, response);
							return; // Evitar despachar 2 veces
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
