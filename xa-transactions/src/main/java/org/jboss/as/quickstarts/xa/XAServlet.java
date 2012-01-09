package org.jboss.as.quickstarts.xa;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
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
 * @author Mike Musgrove
 *
 */
@SuppressWarnings("serial")
@WebServlet("/XA")
public class XAServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head /><body>";

    static String PAGE_CONTENT =
            "<form>"
                    + "<input checked type=\"checkbox\" name=\"em\" value=\"1\" /> Database 1<br />"
                    + "<input checked type=\"checkbox\" name=\"em\" value=\"2\" /> Database 2<br />"
                    + "Key: <input type=\"text\" name=\"key\" /><br />"
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
     * If parameters named "key" and "value" are present then that pair is added (duplicate keys are not allowed) to the database.
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
        String responseText;

        writer.println(PAGE_HEADER);
        writer.println(PAGE_CONTENT);

        responseText = xaService.updateKeyValueDatabase(key, value, ems);

        writer.println("<p>" + responseText + "</p>");
        writer.println(PAGE_FOOTER);

        writer.close();
    }

}
