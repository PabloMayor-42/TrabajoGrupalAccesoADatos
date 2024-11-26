package XML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Pruebas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ArrayList<Incidencia> incidencias = new ArrayList<Incidencia>();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			File archivo = new File("C:\\Users\\rodduraa\\Documents\\Proyectos JEE\\TrabajoGrupalAccesoADatos\\RetoGrupal\\src\\main\\webapp\\incidencias.xml");
			Document documento = builder.parse(archivo);
			Element incidencia = documento.getDocumentElement();
			NodeList lista_tags = incidencia.getElementsByTagName("incidencia");
			for(int i=0;i<lista_tags.getLength();i++) {
				Node tag = lista_tags.item(i);
				
				if (tag.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) tag;
                    System.out.println(elemento.getTextContent());
                }
			}
			
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
