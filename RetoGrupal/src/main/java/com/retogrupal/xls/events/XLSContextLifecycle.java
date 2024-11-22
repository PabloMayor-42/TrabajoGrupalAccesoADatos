package com.retogrupal.xls.events;

import com.retogrupal.xls.ManejoXLS;

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
    	sce.getServletContext().setAttribute("fichero", ManejoXLS.leer(sce.getServletContext().getRealPath("fichero.xls")));
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }
	
}
