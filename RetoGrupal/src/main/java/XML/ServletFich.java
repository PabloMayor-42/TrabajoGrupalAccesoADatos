package XML;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.retogrupal.enitites.Residuo;

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
		//boolean valido = ValidarInfo(request);
			String despachar = "TratamientoFich.jsp";
			if(request.getParameter("accion").equalsIgnoreCase("lectura")) {
				/*Codigo de lectura del fichero XML*/
				try {
					/*Creo un arraylist del Pojo residuo y saco de utilidades la lista de nodos del XML*/
					ArrayList<Residuo> residuos = new ArrayList<Residuo>();
					NodeList lista_nodos = UtilidadXML.LeerXML(getServletContext().getRealPath("recogida-de-residuos-desde-2013.xml"), "row");
					/*Relleno el arraylist*/
					for(int i = 0;i<lista_nodos.getLength();i++) {
						Node nodo = lista_nodos.item(i);
						if(nodo.getNodeType() == Node.ELEMENT_NODE) {
							Element residuo = (Element) nodo;
							LocalDate fecha = LocalDate.parse(residuo.getAttribute("Mes").substring(0, 10),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
							Residuo r = new Residuo(fecha, residuo.getAttribute("Residuo"), residuo.getAttribute("Modalidad"), 
									Double.parseDouble(residuo.getAttribute("Cantidad").replace(',', '.')));
							residuos.add(r);
						}
					}
					//Los guardo en el contexto
					getServletContext().setAttribute("lista_residuos",residuos);
					despachar = "AccesoDatosA.jsp";
				} catch (ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
			}
			else if(request.getParameter("accion").equalsIgnoreCase("escritura")) {
				/*Codigo de escritura de los ficheros XML*/
			}
			request.getRequestDispatcher(despachar).forward(request, response);
	}

//	private boolean ValidarInfo(HttpServletRequest request) {
//		// TODO Auto-generated method stub
//		boolean valido = true;
//		for(int i = 0;i<12;i++) {
//			if(request.getParameter("dato-"+i).isBlank()) valido = false; 
//		}
//		return valido;
//	}

}
