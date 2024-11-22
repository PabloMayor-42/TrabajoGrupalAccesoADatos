package com.retogrupal.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;

import com.retogrupal.xls.entities.DatosXLS;

public class ManejoXLS {
	public static DatosXLS leer(String archivo) {
		DatosXLS datosXLS = null;

		try (FileInputStream fis = new FileInputStream(new File(archivo))) {
			//Cargar hoja principal del excel
			HSSFWorkbook wk = new HSSFWorkbook(fis);
			Sheet hojaPrincipal = wk.getSheetAt(0);

			//Composicion del documento (encabezado y cuerpo)
			ArrayList<String> encabezado = new ArrayList<String>();
			ArrayList<String[]> cuerpo = new ArrayList<String[]>();
			
			//Recorrer contenido
			for(int i = hojaPrincipal.getFirstRowNum(); i < hojaPrincipal.getLastRowNum(); i++) {
				Iterator<Cell> celdas = hojaPrincipal.getRow(i).cellIterator();
				ArrayList<String> fila = new ArrayList<String>();
				
				//Recorrer celdas de cada fila
				while(celdas.hasNext()) {
					Cell celda = celdas.next();
					
					//Selector de objetivo (para agregar las celdas al encabezado o al cuerpo de la tabla
					ArrayList<String> objetivo = ( i == hojaPrincipal.getFirstRowNum() ? encabezado : fila);
					
					//Cada celda puede tener un tipo de dato (se contempla la posibilidad de numericos)
					if(celda.getCellType() == CellType.NUMERIC) {
						objetivo.add(celda.getNumericCellValue() + "");
					}else {
						objetivo.add(celda.getStringCellValue());
					}
				}
				
				//Agregar fila a las filas (solo si no es encabezado)
				if(i != hojaPrincipal.getFirstRowNum())
					cuerpo.add(fila.toArray(new String[0]));
			}
			
			//Crear POJO datos con cabecera y filas
			datosXLS = new DatosXLS(encabezado.toArray(new String[0]), cuerpo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return datosXLS;
	}
}
