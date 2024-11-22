package com.retogrupal.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.retogrupal.xls.entities.DatosXLS;

public class ManejoXLS {
	public static DatosXLS leer(String archivo) {
		DatosXLS datosXLS = null;
		try (FileInputStream fis = new FileInputStream(new File(archivo))) {
			//Cargar hoja principal del excel
			Workbook wk = WorkbookFactory.create(fis);
			Sheet hojaPrincipal = wk.getSheetAt(0);

			ArrayList<String> encabezado = new ArrayList<String>();
			ArrayList<String[]> filas = new ArrayList<String[]>();
			
			//Recorrer contenido
			for(int i = hojaPrincipal.getFirstRowNum(); i < hojaPrincipal.getLastRowNum(); i++) {
				Iterator<Cell> celdas = hojaPrincipal.getRow(i).cellIterator();
				ArrayList<String> fila = new ArrayList<String>();
				
				while(celdas.hasNext())
					( i == hojaPrincipal.getFirstRowNum() ? encabezado : fila).add(celdas.next().getRichStringCellValue().getString());
				
				if(i != hojaPrincipal.getFirstRowNum())
					filas.add((String[])fila.toArray());
			}
			
			//Crear POJO datos con cabecera y filas
			datosXLS = new DatosXLS((String[])encabezado.toArray(), filas);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return datosXLS;
	}
}
