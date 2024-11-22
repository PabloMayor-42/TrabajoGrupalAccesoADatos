package com.retogrupal.xls;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
		switch (request.getParameter("accion") == null ? "" : request.getParameter("accion")) {
		case "lectura":
			despachar = "AccesoDatosA.jsp";
			break;
		case "escritura":
			boolean datoVacio = false;
			Iterator<String> parametros = request.getParameterNames().asIterator();
			ArrayList<Object> datos = new ArrayList<>();

			while (parametros.hasNext() && !datoVacio) {
				String parametro = parametros.next();
				if (parametro.contains("dato-") && !request.getParameter(parametro).isBlank()) {
					try {
						datos.add(Double.parseDouble(request.getParameter(parametro)));
					} catch (NumberFormatException ignore) {
						datos.add(request.getParameter(parametro));
					}

				} else if (parametro.contains("dato-"))
					datoVacio = true;
			}
			
			despachar = datoVacio ? "TratamientoFich.jsp" : "AccesoDatosA.jsp";
			request.setAttribute("faltaParametroFlag", datoVacio);
			
			if (!datoVacio) {
				if(ManejoXLS.escribir((String)getServletContext().getAttribute("fichero-xls"), datos)) {
					getServletContext().setAttribute("fichero-xls-contenido", ManejoXLS.leer((String)getServletContext().getAttribute("fichero-xls")));
				}
				else {
					despachar = "Error.jsp";
					request.setAttribute("error", "Error al realizar la escritura");
				}
			}
			
			break;
		case "":
			request.setAttribute("error", "No se ha seleccionado una opción valida (lectura/scritura)");
			break;
		}

		if (despachar == null)
			despachar = "Error.jsp";

		request.getRequestDispatcher(despachar).forward(request, response);

	}
}
