package com.retogrupal.xls.events;

import com.retogrupal.utils.UtilidadesXLS;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class XLSContextLifecycle
 *
 */
@WebListener
public class XLSContextLifecycle implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public XLSContextLifecycle() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	sce.getServletContext().setAttribute("fichero-xls-contenido", UtilidadesXLS.leer(sce.getServletContext().getRealPath("recogida-de-residuos-desde-2013.xls")));
    	sce.getServletContext().setAttribute("fichero-xls", sce.getServletContext().getRealPath("recogida-de-residuos-desde-2013.xls"));
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }
	
}
