package com.retogrupal.utils;

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

import com.retogrupal.entities.DatosXLS;
import com.retogrupal.entities.Residuo;

public class UtilidadesXLS {
	/**
	 * Realiza la lectura del fichero indicado y devuelve un POJO de DatosXLS con
	 * los datos leidos
	 * 
	 * @param archivo Ruta del archivo a leer
	 * @return POJO de datos leidos (encabezado, POJO de datos por fila)
	 */
	public static DatosXLS leer(String archivo) {
		DatosXLS datosXLS = null;

		try (HSSFWorkbook wk = new HSSFWorkbook(new FileInputStream(new File(archivo)))) {
			// Cargar hoja principal del excel
			Sheet hojaPrincipal = wk.getSheetAt(0);

			// Composicion del documento (encabezado y cuerpo)
			ArrayList<String> encabezado = new ArrayList<String>();
			ArrayList<Residuo> cuerpo = new ArrayList<Residuo>();

			// Recorrer contenido
			for (int i = hojaPrincipal.getFirstRowNum(); i <= hojaPrincipal.getLastRowNum(); i++) {
				Iterator<Cell> celdas = hojaPrincipal.getRow(i).cellIterator();
				Row row = hojaPrincipal.getRow(i);

				// Recorrer celdas de cada fila
				while (celdas.hasNext() && (i == hojaPrincipal.getFirstRowNum())) {
					encabezado.add(celdas.next().getStringCellValue());
				}

				if (i != hojaPrincipal.getFirstRowNum())
					cuerpo.add(new Residuo(
							row.getCell(0).getLocalDateTimeCellValue().toLocalDate(),
							row.getCell(1).getStringCellValue(),
							row.getCell(2).getStringCellValue(),
							row.getCell(3).getNumericCellValue()
							));
			}

			// Crear POJO datos con cabecera y filas
			datosXLS = new DatosXLS(encabezado.toArray(new String[0]), cuerpo);
		} catch (IOException e) {

		}

		return datosXLS;
	}

	/**
	 * Realiza la escritura del fichero indicado y devuelve el estado de la
	 * escritura
	 * 
	 * @param archivo Ruta del archivo a escribir
	 * @return Estado de la operacion (true si se ha realizado y false si ha habido
	 *         algun error)
	 */
	public static boolean escribir(String archivo, Residuo datosFila) {
		// Cargar datos del xsl
		File excel = new File(archivo);

		// Lanzar error si el fichero no existe
		boolean operacionRealizada = excel.exists();

		if (!operacionRealizada)
			return false;

		// Leer de nuevo los datos (puede que el contexto este desactualizado)
		DatosXLS datosXLS = leer(archivo);

		try (HSSFWorkbook wk = new HSSFWorkbook(new FileInputStream(excel))) {
			// Cargar hoja principal del excel
			Sheet hojaPrincipal = wk.getSheetAt(0);

			// Crear nueva fila
			Row fila = hojaPrincipal.createRow(hojaPrincipal.getLastRowNum() + 1);

			// Realizar append de celdas en una nueva fila (solo admite numeros y texto)
			Cell celda0 = fila.createCell(0,CellType.STRING);
			Cell celda1 = fila.createCell(1,CellType.STRING);
			Cell celda2 = fila.createCell(2,CellType.STRING);
			Cell celda3 = fila.createCell(3,CellType.NUMERIC);
			
			celda0.setCellValue(datosFila.getMes());
			celda1.setCellValue(datosFila.getResiduo());
			celda2.setCellValue(datosFila.getModalidad());
			celda3.setCellValue(datosFila.getCantidad());
			
			// Agregar los cambios al xls (reescribir el workbook)
			wk.write(excel);
			
		} catch (IOException e) {
			// Error en la operacion
			operacionRealizada = false;
		}

		return operacionRealizada;
	}
}
