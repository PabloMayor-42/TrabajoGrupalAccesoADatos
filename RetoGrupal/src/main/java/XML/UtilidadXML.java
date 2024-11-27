package XML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UtilidadXML {

	public static NodeList LeerXML(String ruta,String etiqueta) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File archivo = new File(ruta);
		Document documento = builder.parse(archivo);
		Element incidencia = documento.getDocumentElement();
		NodeList lista_tags = incidencia.getElementsByTagName(etiqueta);
		return lista_tags;
	}
	
	
}
