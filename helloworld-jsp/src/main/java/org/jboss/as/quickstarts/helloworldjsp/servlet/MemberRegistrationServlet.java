package org.jboss.as.quickstarts.helloworldjsp.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.as.quickstarts.helloworldjsp.controller.MemberRegistration;
import org.jboss.as.quickstarts.helloworldjsp.data.MemberListProducer;
import org.jboss.as.quickstarts.helloworldjsp.model.Member;

/**
 * Servlet implementation class MemberRegistrationServlet
 */
@WebServlet("/register.do")
public class MemberRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	MemberRegistration registrationService;
	
	@Inject 
	MemberListProducer memberListService;
    
	/**
     * Default constructor. 
     */
    public MemberRegistrationServlet() {
        // TODO Auto-generated constructor stub
    }

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	try {	
		   Member member=registrationService.getNewMember();
		   member.setEmail(request.getParameter("email"));
		   member.setName(request.getParameter("name"));
		   member.setPhoneNumber(request.getParameter("phoneNumber"));
		   registrationService.register();
		   request.setAttribute("infoMessage", member.getName()+" Registered!");
		   request.setAttribute("errorMessage", "");

		   
		   
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
		}
		finally
		{
		 RequestDispatcher resultView = request.getRequestDispatcher("index.jsp");
		 resultView.forward(request,response);
		}
	
	}

}
