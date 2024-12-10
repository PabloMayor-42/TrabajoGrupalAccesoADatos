package com.retogrupal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JFileChooser;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.retogrupal.enitites.Residuo;
import com.retogrupal.utils.RepresentacionTabla;
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
						.leer(getServletContext().getRealPath("recogida-de-residuos-desde-2013.xls"));
				if (tabla != null) {
					request.setAttribute("encabezado", tabla.getEncabezado());
					residuos = tabla.getCuerpo();
				}
				break;
			case "YAML":

				break;
			case "JSON":

				break;
			case "XML":

				break;
			}

			request.setAttribute("residuos", residuos);
			despachar = "AccesoDatosA.jsp";
			break;
		case "escritura":
			switch (fichero) {
			case "CSV":

				break;
			case "XLS":
				DateTimeFormatter formateadorAuxiliar = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				Iterator<String> parametros = request.getParameterNames().asIterator();

				ArrayList<String> datos = new ArrayList<>();

				boolean datoVacio = false;
				while (parametros.hasNext() && !datoVacio) {
					String parametro = parametros.next();

					// Si tiene el prefijo "dato-" corresponde a los input reservados para agregar
					// datos al fichero (solo tiene sentido agregar si los parametros procesados
					// tienen datos)
					if (parametro.contains("dato") && !request.getParameter(parametro).isBlank() && !datoVacio)
						datos.add(request.getParameter(parametro));
					else if (parametro.contains("dato")) // Parametros sin valor asociado
						datoVacio = true;
				}

				if (datoVacio) {
					despachar = "TratamientoFich.jsp";
					request.setAttribute("faltaParametroFlag", datoVacio);
				} else {
					boolean estado;

					try {
						estado = UtilidadesXLS.escribir(
								getServletContext().getRealPath("recogida-de-residuos-desde-2013.xls"),
								new Residuo(LocalDate.parse(datos.get(0), formateadorAuxiliar), datos.get(1),
										datos.get(2), Double.parseDouble(datos.get(3))));
					} catch (DateTimeParseException e) {
						estado = false;
						request.setAttribute("error", "Fecha proporcionada incorrecta (formato de dato 1: yyyy-mm-dd)");
					} catch (NumberFormatException e) {
						estado = false;
						request.setAttribute("error", "Numero proporcionado incorrecto (dato 4)");
					}

					if (!estado) {
						despachar = "";
						request.setAttribute("error", "Error al realizar la escritura sobre el fichero XLS");
					}
				}

				break;
			case "YAML":

				break;
			case "JSON":

				break;
			case "XML":

				break;
			}
			break;
		}

		if (despachar == null) {
			despachar = "Error.jsp";
		}

		request.getRequestDispatcher(despachar).forward(request, response);
	}

}
