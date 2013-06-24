package org.jboss.as.quickstarts.hibernate_search.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="FeedServiceServlet", urlPatterns={"/feedService"})
public class FeedServiceServlet extends HttpServlet {


    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Display the list of guests:
        request.setAttribute("guests", "");
        request.getRequestDispatcher("/guest.jsp").forward(request, response);
    }

    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Handle a new guest:
        String name = request.getParameter("name");
        doGet(request, response);
    }
}
