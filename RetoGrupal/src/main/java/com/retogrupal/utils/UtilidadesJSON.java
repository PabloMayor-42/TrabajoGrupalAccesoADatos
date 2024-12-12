package com.retogrupal.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.retogrupal.enitites.Residuo;

public class UtilidadesJSON {
	private record ResiduosAdapter(String Mes,String Residuo, String Modalidad, String Cantidad) {}
	
	public static RepresentacionTabla leer(String archivo) {
		RepresentacionTabla datosJSON = new RepresentacionTabla(new ArrayList<String>(),new ArrayList<Residuo>());
		
		try(FileReader fr = new FileReader(archivo)){
			for(ResiduosAdapter adapter : new Gson().fromJson(fr, ResiduosAdapter[].class))
				datosJSON.getCuerpo().add(new Residuo(
						LocalDate.parse(adapter.Mes(),DateTimeFormatter.ISO_LOCAL_DATE_TIME),
						adapter.Residuo(),
						adapter.Modalidad(),
						Double.parseDouble(adapter.Cantidad().replace(',', '.'))));
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		return datosJSON;
	}

	public static boolean escribir(String archivo, Residuo datosFila) {	
		ArrayList<Residuo> cuerpo = UtilidadesJSON.leer(archivo).getCuerpo();
		
		boolean realizado = new File(archivo).exists();
		
		try(FileWriter fw = new FileWriter(archivo)){
			cuerpo.add(datosFila);
			
			ArrayList<ResiduosAdapter> adaptadorCuerpo = new ArrayList<UtilidadesJSON.ResiduosAdapter>();
			for(Residuo residuo : cuerpo)
				adaptadorCuerpo.add(new ResiduosAdapter(
						residuo.getMes().toString() + "T00:00", 
						residuo.getResiduo(), 
						residuo.getModalidad(), 
						residuo.getCantidad() + ""));
			
			fw.write(new Gson().toJson(adaptadorCuerpo.toArray()));
			
		}catch (IOException e) {
			e.printStackTrace();
			realizado = false;
		}
		
		return realizado;


	}
}
