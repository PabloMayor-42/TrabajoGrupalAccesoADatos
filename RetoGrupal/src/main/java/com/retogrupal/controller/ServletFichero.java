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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.retogrupal.enitites.Residuo;

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

							getServletContext().setAttribute("encabezado", encabezado);

						}
						i++;
					}

				} catch (CsvValidationException | IOException e) {
					// TODO Auto-generated catch block
					String error = e.getMessage();
					request.setAttribute("error", error);
					despachar = "Error.jsp";
				}
				reader.close();
				break;
			case "XLS":

				break;
			case "ODS":

				File f = new File(
						getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.ods"));
				try (OdfSpreadsheetDocument document = OdfSpreadsheetDocument.loadDocument(f)) {
					OdfTable hoja = document.getSpreadsheetTables().get(0);

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

					System.out.println(encabezado);

					getServletContext().setAttribute("encabezado", encabezado);

					// Leer datos
					for (int row = 1; row < hoja.getRowCount(); row++) {
			
						// Accede a los valores individuales
						DateTimeFormatter frmt2 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
						LocalDate fecha = LocalDateTime.parse(hoja.getCellByPosition(0, row).getStringValue(), frmt2)
								.toLocalDate();
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
				}

				break;
			case "JSON":

				break;
			case "XML":

				break;
			}

			getServletContext().setAttribute("residuos", residuos);
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

						String[] datos = {
								(fecha + "T00:00," + tipoResiduo + "," + modalidad + ",\"" + cantidad + "\"") };
						writer.writeNext(datos);
						writer.close();
						despachar = "TratamientoFich.jsp";

						break;
					case "XLS":

						break;
					case "ODS":

						File f = new File(
								getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.ods"));
						try {
							OdfSpreadsheetDocument document = OdfSpreadsheetDocument.loadDocument(f);

							// Obtener la primera hoja por índice
							OdfTable hoja = document.getSpreadsheetTables().get(0);

							int nextFila=0;
							
							for (int row = 1; row < hoja.getRowCount(); row++) {
								String dato= hoja.getCellByPosition(0, row).getStringValue();
								nextFila++;
								if(dato== null || dato.isEmpty()) {
									row=hoja.getRowCount();
								}
								
							}
							
							System.out.println(hoja.getCellByPosition(0, nextFila).getStringValue() + "           Muestra algo");
							System.out.println(hoja.getCellByPosition(1, nextFila - 1).getStringValue());
							System.out.println(hoja.getCellByPosition(2, nextFila - 1).getStringValue());
							System.out.println(hoja.getCellByPosition(3, nextFila).getStringValue());

							
							 // Agregar más datos en la siguiente fila 
							 hoja.getCellByPosition(0, nextFila-1).setDateValue();
							 hoja.getCellByPosition(1, nextFila-1).setStringValue(tipoResiduo);
							 hoja.getCellByPosition(2, nextFila-1).setStringValue(modalidad);
							 hoja.getCellByPosition(3, nextFila-1).setDoubleValue(Double.parseDouble(cantidad.replace(",", ".")));

							 // Guardar los cambios en el mismo archivo 
							 document.save(f);
							 
							 System.out.println("Archivo modificado exitosamente: " +
							 f.getAbsolutePath());
							 
							document.close();
							despachar = "TratamientoFich.jsp";
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					case "JSON":

						break;
					case "XML":

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

		request.getRequestDispatcher(despachar).forward(request, response);
	}

}
