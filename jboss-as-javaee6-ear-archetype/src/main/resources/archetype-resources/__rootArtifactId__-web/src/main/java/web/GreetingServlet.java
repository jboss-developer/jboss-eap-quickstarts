#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ${package}.ejb.HelloService;

/**
 * Greeting servlet
 */
public class GreetingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private HelloService helloService;
	
	/**
	 * @see HttpServlet${symbol_pound}doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String greeting = helloService.greet(name);
		request.setAttribute("greeting", greeting);
		getServletContext().getRequestDispatcher("/index.jsp").forward(request, response); //${symbol_dollar}NON-NLS-1${symbol_dollar}
	}

	/**
	 * @see HttpServlet${symbol_pound}doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
