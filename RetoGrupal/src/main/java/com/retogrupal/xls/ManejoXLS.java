package com.retogrupal.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.retogrupal.xls.entities.DatosXLS;

public class ManejoXLS {
	public static DatosXLS leer(String archivo) {
		DatosXLS datosXLS = null;

		try (HSSFWorkbook wk = new HSSFWorkbook(new FileInputStream(new File(archivo)))) {
			// Cargar hoja principal del excel
			Sheet hojaPrincipal = wk.getSheetAt(0);

			// Composicion del documento (encabezado y cuerpo)
			ArrayList<String> encabezado = new ArrayList<String>();
			ArrayList<Object[]> cuerpo = new ArrayList<Object[]>();

			// Recorrer contenido
			for (int i = hojaPrincipal.getFirstRowNum(); i < hojaPrincipal.getLastRowNum(); i++) {
				Iterator<Cell> celdas = hojaPrincipal.getRow(i).cellIterator();
				ArrayList<Object> fila = new ArrayList<Object>();

				// Recorrer celdas de cada fila
				while (celdas.hasNext()) {
					Cell celda = celdas.next();

					// Selector de objetivo (para agregar las celdas al encabezado o al cuerpo de la
					// tabla
					ArrayList objetivo = (i == hojaPrincipal.getFirstRowNum() ? encabezado : fila);

					// Cada celda puede tener un tipo de dato (se contempla la posibilidad de
					// numericos)
					if (celda.getCellType() == CellType.NUMERIC) {
						objetivo.add((Double)celda.getNumericCellValue());
					} else {
						objetivo.add(celda.getStringCellValue());
					}
				}

				// Agregar fila a las filas (solo si no es encabezado)
				if (i != hojaPrincipal.getFirstRowNum())
					cuerpo.add(fila.toArray(new Object[0]));
			}

			// Crear POJO datos con cabecera y filas
			datosXLS = new DatosXLS(encabezado.toArray(new String[0]), cuerpo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return datosXLS;
	}

	public static boolean escribir(String archivo, ArrayList<Object> datosFila) {
		//Cargar datos del xsl
		File excel = new File(archivo);
		
		//Lanzar error si el fichero no existe
		boolean operacionRealizada = excel.exists();

		if (!operacionRealizada)
			return false;

		//Leer de nuevo los datos (puede que el contexto este desactualizado)
		DatosXLS datosXLS = leer(archivo);
		
		try (HSSFWorkbook wk = new HSSFWorkbook(new FileInputStream(excel))) {
			// Cargar hoja principal del excel
			Sheet hojaPrincipal = wk.getSheetAt(0);
			
			//Crear nueva fila
			Row fila = hojaPrincipal.createRow(hojaPrincipal.getLastRowNum()+1);
			
			//Realizar append de celdas en una nueva fila (solo admite numeros y texto)
			for (int i = 0; i < datosFila.size(); i++) {
				Cell celda = fila.createCell(i,
						datosFila.get(i) instanceof Double ? CellType.NUMERIC : CellType.STRING);

				//Establecer el valor de celda y tiparlo (solo se tipan numeros y strings)
				if (datosFila.get(i) instanceof Double)
					celda.setCellValue((Double) datosFila.get(i));
				else
					celda.setCellValue((String) datosFila.get(i));
			}
			
			//Agregar los cambios al xls (reescribir el workbook)
			try (FileOutputStream fos = new FileOutputStream(excel)) {
                wk.write(fos);
            }

		} catch (IOException e) {
			//Error en la operacion
			e.printStackTrace();
			operacionRealizada = false;
		}
		
		return operacionRealizada;
	}
}
