package com.retogrupal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/*File prueba=new File("RetoGrupal/scr/main/resources/atmosfera_inventario_emisiones.csv");
		FileReader p=new FileReader(prueba);
		System.out.println(p);*/
		/*JFileChooser fich=new JFileChooser();
		fich.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = fich.showOpenDialog(null);
		File file= fich.getSelectedFile();
		System.out.println(file.getPath());*/
		String realpath=getServletContext().getRealPath("WEB-INF/classes/atmosfera_inventario_emisiones.csv");
		System.out.println("Real path: " + realpath);
		 File file = new File(realpath);
	        if (file.exists()) {
	            // Aquí podrías leer el archivo
	            System.out.println("Archivo encontrado.");
	        } else {
	            System.out.println("Archivo no encontrado.");
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//File prueba=new File("RetoGrupal/scr/main/resources/atmosfera_inventario_emisiones.csv");
		//System.out.println(prueba);
		
		String despachar = null;
		String fichero=request.getParameter("fichero");
		switch (request.getParameter("accion")) {
		case "lectura":
			if(fichero.equals("CSV")) {
				
				CSVReader reader=new CSVReader(new FileReader(getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv")));
				String[] nextLine;
		        try {
					while ((nextLine = reader.readNext()) != null) {
					    System.out.println(Arrays.toString(nextLine));
					}
				} catch (CsvValidationException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        reader.close();
			}
			despachar = "AccesoDatosA.jsp";
			break;
		case "escritura":
			if(fichero.equals("CSV")) {
				
			}
			break;
		}

		if (despachar == null)
			despachar = "Error.jsp";

		request.getRequestDispatcher(despachar).forward(request, response);
	}

}
