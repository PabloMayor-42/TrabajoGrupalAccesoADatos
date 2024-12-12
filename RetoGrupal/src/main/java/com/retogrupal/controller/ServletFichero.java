package com.retogrupal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
		//Fichero al que se accedera segun lo seleccionado
		String despachar = null;
		
		//fichero que se desea leer/escribir
		String fichero = request.getParameter("fichero");
		
		//Segun sea Leer o escribir se hara una cosa u otra

		DateTimeFormatter frmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		switch (request.getParameter("accion")) {
		case "lectura":
			//Leemos el tipo de fichero seleccionado
			ArrayList<Residuo> residuos = new ArrayList();
			switch (fichero) {
			case "CSV": 
				//Creamos un CSVReader para leer el fichero CSV
				//Con getRealPath seleccionamos la ruta del fichero
				CSVReader reader = new CSVReader(new FileReader(
						getServletContext().getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv")));
				String[] nextLine;
				int i = 0;
				try {
					//Leemos las lineas del csv individualmente
					while ((nextLine = reader.readNext()) != null) {
						if (i > 0) {
							
							// Accede a los valores individuales
							LocalDate fecha = LocalDateTime.parse(nextLine[0], frmt).toLocalDate(); 
							String tipoResiduo = nextLine[1]; 
							String modalidad = nextLine[2]; 
							Double cantidad = Double.parseDouble(nextLine[3].replace(",", "."));
							
							//Creamos un nuevo Residuo
							Residuo dato=new Residuo(fecha, tipoResiduo, modalidad, cantidad);
							//Añadimos el residuo a la lista
							residuos.add(dato);
							
						} else {
							//La primera linea es el encabezado por lo que lo guardamos aparte
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
				
				break;
			case "YAML":
				
				break;
			case "JSON":
				////
				
				//PREGUNTAR POR LAS RUTAS
				String json =getServletContext().getRealPath("/recogida-de-residuos-desde-2013.json");
				
				System.out.println("Ruta completa del archivo JSON: " + json);

				
				
				try {
			          FileReader fr = new FileReader(json);
			          ArrayList<String> cabeceras = new ArrayList<>();
				         
			          /*
			          //Leer el archivo Json con Gson para trabnajar con la librera
			          Gson gson = new Gson();
			          //convierte el contenido del archivo Json a un JsonArray
			          JsonArray jsonArray = gson.fromJson(fr, JsonArray.class);

			          //erificar si el archivo está vacio
			          if (jsonArray == null || jsonArray.size() == 0) {
			              System.out.println("El archivo está vacio");
			              return;
			          }

			          
			          
                        //Recoge las cabeceras del primer objeto Json
                        JsonObject primerObj = jsonArray.get(0).getAsJsonObject();
                        
                        for (String temp : primerObj.keySet()) {
                            
                        	cabeceras.add(temp);
                            
                        }*/

                        for (Residuo res : new Gson().fromJson(fr, Residuo[].class)) {
                        	residuos.add(res);
                        }
                    
                        /*
                        
                        // Recoger el contenido
                        for (i = 0; i < jsonArray.size(); i++) {
                        	
                            JsonObject fila = jsonArray.get(i).getAsJsonObject();
                            
                            LocalDate fecha = LocalDateTime.parse(fila.get("Mes").getAsString(), frmt).toLocalDate();
                            String residuo = (String) fila.get("Residuo").getAsString();
                            String modalidad = (String) fila.get("Modalidad").getAsString();
                            String cantidad = (String) fila.get("Cantidad").getAsString();
                            
                            System.out.println(fecha);
                            
                            //lee todo bn pero a la hora de hacer el res me lo coge de null 
                            //crear el objeto Residuo y agrearlo a la lista
                            
                            
                            Residuo res = new Residuo(fecha, residuo, modalidad, cantidad);
                            residuos.add(res);
                        }
                        */
                        
                        
						//enviiaar los datos
                  
                        request.setAttribute("encabezado", cabeceras);
                        request.setAttribute("residuos", residuos);
                        request.getRequestDispatcher("AccesoDatosA.jsp").forward(request, response);

			      } catch (IOException e) {
			          System.out.print("No lo encuentra");
			      }				
				///
				break;
			case "XML":
				
				break;
			}
			
			 request.setAttribute("residuos", residuos);
			despachar = "AccesoDatosA.jsp";
			break;
			
			
			
			
			
			
			
			
			
			
			
		case "escritura":
			String fecha = request.getParameter("dato1");
			String tipoResiduo = request.getParameter("dato2");
			String modalidad = request.getParameter("dato3");
			String cantidad = "\""+request.getParameter("dato4")+"\"";

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
						String path=getServletContext()
								.getRealPath("WEB-INF/classes/recogida-de-residuos-desde-2013.csv");
						System.out.println(path);
						
						CSVWriter writer=new CSVWriter(new FileWriter(path,true));
						
						String [] datos= {(fecha+"T00:00,"+tipoResiduo+","+modalidad+","+cantidad)};
						writer.writeNext(datos);
						writer.close();
						despachar = "TratamientoFich.jsp";
						
						break;
					case "XLS":

						break;
					case "YAML":

						break;
					case "JSON":
					
						JsonArray jsonArray=new JsonArray();
						
						JsonObject jsonObject = new JsonObject();
						
					    jsonObject.addProperty("mes", fecha );
					    jsonObject.addProperty("residuo", tipoResiduo);
					    jsonObject.addProperty("modalidad", modalidad);
					    jsonObject.addProperty("cantidad", cantidad);
					    jsonArray.add(jsonObject);
						
					    
					    FileWriter fw=new FileWriter("/Archivos/archivo.json");
					    Gson gson = new Gson();
			            gson.toJson(jsonArray, fw);
			            response.getWriter().append("Archivo JSON escrito exitosamente.");
						despachar = "TratamientoFich.jsp";
						break;
					
		}
			
		if (despachar == null) {
			despachar = "Error.jsp";
		}

		System.out.println(despachar);
		request.getRequestDispatcher(despachar).forward(request, response);
	}catch (Exception e) {
		// TODO: handle exception
	}

}}}}

	
