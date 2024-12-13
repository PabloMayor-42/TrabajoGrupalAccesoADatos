package com.retogrupal.utils;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.retogrupal.enitites.Residuo;

public class LecturaCSV {

	public static ArrayList<String> EncabezadoCSV(FileReader f) throws CsvValidationException, IOException {
		// Creamos un CSVReader para leer el fichero CSV
		CSVReader reader = new CSVReader(f);

		String[] nextLine;
		DateTimeFormatter frmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		int i = 0;
		ArrayList<String> encabezado = new ArrayList<>();
		while ((nextLine = reader.readNext()) != null) {
			// La primera linea es el encabezado por lo que lo guardamos aparte
			String fecha = nextLine[0];
			String tipoResiduo = nextLine[1];
			String modalidad = nextLine[2];
			String cantidad = nextLine[3];

			
			encabezado.add(fecha);
			encabezado.add(tipoResiduo);
			encabezado.add(modalidad);
			encabezado.add(cantidad);
			i++;
			reader.close();
			return encabezado;
		}
		return encabezado;

	}

	public static ArrayList<Residuo> LeerDatos(FileReader f) throws CsvValidationException, NumberFormatException, IOException {

		CSVReader reader = new CSVReader(f);

		String[] nextLine;
		DateTimeFormatter frmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		int i = 0;
		ArrayList<Residuo> lisRes=new ArrayList<Residuo>();
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
				lisRes.add(dato);
			}
			i++;
		}
		reader.close();
		return lisRes;

	}

}
