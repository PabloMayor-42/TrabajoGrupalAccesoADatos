package com.retogrupal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.retogrupal.utils.UtilidadesXLS;
import com.retogrupal.xls.entities.DatosXLS;

/**
 * Servlet implementation class ServletFich
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletFich() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String despachar = null;

		// Realizar operaciones en base a opcion seleccionada (lectura / escritura)
		switch (request.getParameter("accion") == null ? "" : request.getParameter("accion")) {
		case "lectura":
			// Tomar formato y cargar el contenido que se haya seleccionado en la request
			if (request.getParameter("formato") != null) {
				despachar = "AccesoDatosA.jsp";
				cargaContenido(request);
			} else {
				// Formato nulo
				despachar = "Error.JSP";
				request.setAttribute("error", "No se ha seleccionado un formato");
			}
			break;
		case "escritura":
			// Tomar formato y realizar escritura en su fichero correspondiente (cargado en
			// contexto como fichero-<<formato>>)
			if (request.getParameter("formato") != null)
				despachar = realizarEscritura(request.getParameter("formato").toLowerCase(), request, response);
			else {
				// Formato nulo
				despachar = "Error.jsp";
				request.setAttribute("error", "No se ha seleccionado un formato");
			}
			break;
		default:
			// Operacion no indicada
			request.setAttribute("error", "No se ha seleccionado una opción valida (lectura/escritura)");
			break;
		}

		// En el caso de que no haya despachado (error producido) despacha a Error
		if (despachar == null)
			despachar = "Error.jsp";

		request.getRequestDispatcher(despachar).forward(request, response);
	}

	/**
	 * Carga el contenido en los atributos de la request en base al formato
	 * almacenado en los parametros de la request
	 * 
	 * @param request Solicitud entrante
	 */
	private void cargaContenido(HttpServletRequest request) {
		request.setAttribute("contenido", getServletContext()
				.getAttribute("fichero-" + request.getParameter("formato").toLowerCase() + "-contenido"));
	}

	/**
	 * Realiza la escritura en el fichero relacionado al tipo de formato
	 * 
	 * @param formato  Nombre del formato escrito en minúsculas. Ejemplo: xls
	 * @param request  Solicitud entrante
	 * @param response Respuesta del servlet
	 * @return Nombre de la vista a la que despachará una vez intentada la escritura
	 */
	private String realizarEscritura(String formato, HttpServletRequest request, HttpServletResponse response) {
		String despachar = "";

		boolean datoVacio = false;

		// Toma todos los nombres de los parametros enviados en la request
		Iterator<String> parametros = request.getParameterNames().asIterator();

		// Contiene la line que se ira leyendo. Es de tipo object para poder tipar los
		// datos (numeros y strings)
		ArrayList<Object> datos = new ArrayList<>();

		while (parametros.hasNext() && !datoVacio) {
			String parametro = parametros.next();

			// Si tiene el prefijo "dato-" corresponde a los input reservados para agregar
			// datos al fichero (solo tiene sentido agregar si los parametros procesados tienen datos)
			if (parametro.contains("dato-") && !request.getParameter(parametro).isBlank() && !datoVacio) {
				// Formateo de datos (int / string)
				try {
					datos.add(Double.parseDouble(request.getParameter(parametro)));
				} catch (NumberFormatException ignore) {
					datos.add(request.getParameter(parametro));
				}

			} else if (parametro.contains("dato-")) // Parametros sin valor asociado
				datoVacio = true;
		}

		// Despachar a datos si falta de dar algun dato
		despachar = datoVacio ? "TratamientoFich.jsp" : "AccesoDatosA.jsp";
		request.setAttribute("faltaParametroFlag", datoVacio);

		if (!datoVacio) {
			boolean resultadoOperacion = false; // True = satisfactoria, False = Error en operacion

			switch (formato) {
			case "xls":
				// Realizar escritura
				resultadoOperacion = UtilidadesXLS
						.escribir((String) getServletContext().getAttribute("fichero-" + formato), datos);

				// En caso de operacion satisfactoria, cargar nuevos datos en el contexto
				if (resultadoOperacion) {
					getServletContext().setAttribute("fichero-" + formato + "-contenido",
							UtilidadesXLS.leer((String) getServletContext().getAttribute("fichero-" + formato)));
				}
				break;
			// TODO Agregar comunicacion con utilidades de lectura con archivos
			}

			// Procesar resultado de la operacion (lanzar error o cargar contenido escrito)
			if (resultadoOperacion) {
				cargaContenido(request);
			} else {
				despachar = "Error.jsp";
				request.setAttribute("error", "Error al realizar la escritura");
			}
		}

		return despachar;
	}
}
