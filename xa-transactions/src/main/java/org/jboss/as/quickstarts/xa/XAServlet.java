package org.jboss.as.quickstarts.xa;

import java.io.*;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.inject.Inject;
import javax.naming.ldap.PagedResultsResponseControl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A servlet for triggering updates to two different databases within a single XA transaction.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /XA using the {@linkplain WebServlet
 * @HttpServlet}. The {@link XAService} is injected by CDI.
 * </p>
 *
 * @author Michael Musgrove
 *
 */
@SuppressWarnings("serial")
@WebServlet("/XA")
public class XAServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>XA Recovery</title></head><body>";

    static String PAGE_CONTENT = "<h2>XA Recovery</h2>"
            + "<p>This example demonstrates how to update two databases within the same transaction and how to recovery from failures.<br />"
            + "Refer to the <a href=\"readme.html\">readme</a> for full instructions.</p>"
            + "<form>"
            + "<input checked type=\"checkbox\" name=\"em\" value=\"1\" /> Database 1"
            + "&nbsp;(Check to include this database within the transaction)<br />"
            + "<input checked type=\"checkbox\" name=\"em\" value=\"2\" /> Database 2"
            + "&nbsp;(Check to include this database within the transaction)<br />"
            + "Key: <input type=\"text\" name=\"key\" />"
            + "Leave black to list all key/value pairs in each selected database<br />"
            + "Value: <input type=\"text\" name=\"value\" /><br />"
            + "<input type=\"submit\" value=\"Submit\" /><br />"
            + "</form>";

    static String PAGE_FOOTER = "</body></html>";

    @Inject
    XAService xaService;

    /**
     * <p>Servlet entry point.
     * </p>
     * <p>The behaviour of the servlet is controlled by servlet query parameters.
     * If parameters named "key" and "value" are present then that pair is added (or updated) to the database.
     * A parameter named em controls which databases will be included in the update.
     * </p>
     * @param req the HTTP request
     * @param resp the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String key = req.getParameter("key");
        String value = req.getParameter("value");
        String[] ems = req.getParameterValues("em");
        String responseText = "";

        writer.println(PAGE_HEADER);
        writer.println(PAGE_CONTENT);

        if (ems != null)
            responseText = xaService.updateKeyValueDatabase(key, value, ems);

        writer.println("<p>" + responseText + "</p>");

        writer.println(PAGE_FOOTER);

        writer.close();
    }
}
