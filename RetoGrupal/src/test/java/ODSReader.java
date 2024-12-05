import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;

public class ODSReader {

	public static List<String> encabezados(String filePath) {
		
	    List<String> movieList = new ArrayList<String>();
	    
	    try {
	        SpreadSheet spread = new SpreadSheet(new File(filePath));
	        Sheet sheet = spread.getSheets().get(0);
	        Range movies = sheet.getDataRange();
	        Object[][] movieColumn = movies.getValues();
	        for (int i = 0; i < sheet.getMaxColumns(); i++) {
	            movieList.add(movieColumn[0][i].toString());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return movieList;
	}
	
	
	public static List<String> datosColumna(String filePath) {
		
	    List<String> movieList = new ArrayList<String>();
	    
	    try {
	        SpreadSheet spread = new SpreadSheet(new File(filePath));
	        Sheet sheet = spread.getSheets().get(0);
	        Range movies = sheet.getDataRange();
	        Object[][] movieColumn = movies.getValues();
	        for (int i = 1; i < sheet.getMaxRows()-1; i++) {
	        	movieList.add(movieColumn[i][0].toString());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return movieList;
	}
	
	public static List<String> datosTotales(String filePath) {
		
	    List<String> movieList = new ArrayList<String>();
	    
	    try {
	        SpreadSheet spread = new SpreadSheet(new File(filePath));
	        Sheet sheet = spread.getSheets().get(0);
	        Range movies = sheet.getDataRange();
	        Object[][] movieColumn = movies.getValues();
	        for (int i = 1; i < sheet.getMaxRows()-1; i++) {
	        	for (int j = 0; j < sheet.getMaxColumns(); j++) {
	        		movieList.add(movieColumn[i][j].toString());
				}
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return movieList;
	}
}
