

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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.retogrupal.enitites.Residuo;
import com.retogrupal.utils.UtilidadXML;

/**
 * Servlet implementation class XML
 */
@WebServlet("/ServletFichero")
public class ServletFichero extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFichero() {
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
					ArrayList<Residuo> residuos = UtilidadXML.LeerXML(getServletContext().getRealPath("recogida-de-residuos-desde-2013.xml"), "row");;
					getServletContext().setAttribute("residuos",residuos);
					getServletContext().setAttribute("encabezado", UtilidadXML.CargarEncabezados());
					despachar = "AccesoDatosA.jsp";
					
				} catch (ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
			}
			else if(request.getParameter("accion").equalsIgnoreCase("escritura")) {
				/*Codigo de escritura de los ficheros XML*/
				LocalDate fecha = LocalDate.parse(request.getParameter("dato1"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String residuo = request.getParameter("dato2");
				String modalidad = request.getParameter("dato3");
				double cantidad = Double.parseDouble(request.getParameter("dato4"));
				Residuo r = new Residuo(fecha, residuo, modalidad, cantidad);
				try {
					UtilidadXML.EscribirXML(getServletContext().getRealPath("recogida-de-residuos-desde-2013.xml"),r);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
