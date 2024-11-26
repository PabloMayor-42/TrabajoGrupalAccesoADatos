package XML;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

/**
 * Servlet implementation class XML
 */
@WebServlet("/ServletFich")
public class ServletFich extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFich() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean valido = ValidarInfo(request);
		String despachar = "TratamientoFich.jsp";
		if(valido) {
			if(request.getParameter("accion").equalsIgnoreCase("lectura")) {
				/*Codigo de lectura del fichero XML*/
				try {
					ArrayList<Incidencia> incidencias = new ArrayList<Incidencia>();
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					File archivo = new File(getServletContext().getRealPath("fichero.xml"));
					Document documento = builder.parse(archivo);
					Element incidencia = documento.getDocumentElement();
					NodeList lista_tags = incidencia.getChildNodes();
					for(int i=0;i<lista_tags.getLength();i++) {
						Node tag = lista_tags.item(i);
						Element resultado = (Element) tag;
						System.out.println(tag.getLocalName()+": "+resultado.getTextContent());
					}
					
				} catch (ParserConfigurationException | SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			else if(request.getParameter("accion").equalsIgnoreCase("escritura")) {
				/*Codigo de escritura de los ficheros XML*/
			}
		}
		else {
			getServletContext().setAttribute("faltaParametroFlag", true);
		}
		
		request.getRequestDispatcher(despachar).forward(request, response);
	}

	private boolean ValidarInfo(HttpServletRequest request) {
		// TODO Auto-generated method stub
		boolean valido = true;
		for(int i = 0;i<12;i++) {
			if(request.getParameter("dato-"+i).isBlank()) valido = false; 
		}
		return valido;
	}

}
