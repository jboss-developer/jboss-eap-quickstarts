/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.mbeanhelloworld.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple servlet 3 as client that use mbean service.
 * </p>
 * 
 * <p>
 * The servlet is registered and mapped to /HelloWorldMBeanServletClient using the {@linkplain WebServlet @HttpServlet}.
 * </p>
 * 
 * @author Jérémie Lagarde
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/HelloWorldMBeanServletClient")
public class HelloWorldMBeanServletClient extends HttpServlet {

    private MBeanServer mbeanServer;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        writeHeader(out);
        out.write("<h1>Quickstart: Example demonstrates the use of MBean in JBoss AS 7.</h1>");
        out.write("<p>This quickstart demonstrates how to build a welcome message using different MBeans. For each MBean, enter a welcome message and click \"save\", then enter a name and click the \"sayHello\" button. The counter will be incremented and the generated message will be displayed.</p>");
        try {
            if (mbeanServer == null)
                mbeanServer = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> names = mbeanServer.queryNames(null, null);

            for (ObjectName objectName : names) {
                if (objectName.getDomain().equals("quickstarts")) {
                    writeMBean(req, out, objectName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("<h2>A problem occurred during the delivery of this message</h2>");
            out.write("</br>");
            out.write("<p><i>Go your the JBoss Application Server console or Server log to see the error stack trace</i></p>");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void writeMBean(HttpServletRequest req, PrintWriter out, ObjectName objectName) throws ReflectionException,
            IntrospectionException, InstanceNotFoundException, InvalidAttributeValueException, MBeanException,
            AttributeNotFoundException {
        String submit = (String) req.getParameter("submit");
        String mbean = (String) req.getParameter("mbean");
        String attribute = (String) req.getParameter("attribute");
        String operation = (String) req.getParameter("operation");

        MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(objectName);
        out.write("<div class=\"mbean\"><b title=\"" + objectName.getCanonicalName() + "\">"
                + objectName.getKeyProperty("type") + "</b>");
        String callResult = null;
        if (objectName.getKeyProperty("type").equals(mbean)) {
            if (attribute != null && "save".equals(submit)) {
                saveAttribute(req, objectName, attribute);
            } else if (operation != null && operation.equals(submit)) {
                callResult = callOperation(req, objectName, operation, mbeanInfo, callResult);
            }
        }
        writeAttributes(out, objectName, mbeanInfo);
        writeOperations(out, mbean, operation, objectName, mbeanInfo, callResult);
        out.write("</div>\n");
    }

    private String callOperation(HttpServletRequest req, ObjectName objectName, String operation, MBeanInfo mbeanInfo,
            String callResult) throws ReflectionException, InstanceNotFoundException, MBeanException {
        for (MBeanOperationInfo operationInfo : mbeanInfo.getOperations()) {
            if (operationInfo.getName().equals(operation)) {
                List<String> params = new ArrayList<String>();
                List<String> signature = new ArrayList<String>();
                for (MBeanParameterInfo parameterInfo : operationInfo.getSignature()) {
                    params.add((String) req.getParameter(parameterInfo.getName()));
                    signature.add(parameterInfo.getType());
                }
                callResult = (String) mbeanServer.invoke(objectName, operation, params.toArray(),
                        signature.toArray(new String[signature.size()]));
            }
        }
        return callResult;
    }

    private void saveAttribute(HttpServletRequest req, ObjectName objectName, String attribute)
            throws InstanceNotFoundException, InvalidAttributeValueException, ReflectionException, MBeanException,
            AttributeNotFoundException {
        String value = (String) req.getParameter("value");
        Attribute attr = new Attribute(attribute, value);
        mbeanServer.setAttribute(objectName, attr);
    }

    private void writeOperations(PrintWriter out, String mbean, String operation, ObjectName objectName, MBeanInfo mbeanInfo,
            String callResult) {
        if (mbeanInfo.getOperations().length > 0) {
            for (MBeanOperationInfo operationInfo : mbeanInfo.getOperations()) {
                writeOperation(out, mbean, operation, objectName, callResult, operationInfo);
            }
        }
    }

    private void writeOperation(PrintWriter out, String mbean, String operation, ObjectName objectName, String callResult,
            MBeanOperationInfo operationInfo) {
        String mbeanName = objectName.getKeyProperty("type");
        out.write("<div><form>");
        out.write("<input type=\"hidden\" name=\"mbean\" value=\"" + mbeanName + "\" />");
        out.write("<input type=\"hidden\" name=\"operation\" value=\"" + operationInfo.getName() + "\" />");
        for (MBeanParameterInfo parameterInfo : operationInfo.getSignature()) {
            out.write(" <input type=\"text\" name=\"" + parameterInfo.getName() + "\" value=\"\" title=\""
                    + parameterInfo.getName() + " ( " + parameterInfo.getType() + " )\"/>");
        }
        out.write("<input type=\"submit\" name=\"submit\" value=\"" + operationInfo.getName() + "\"/>");
        if (mbeanName.equals(mbean) && operationInfo.getName().equals(operation)) {
            out.write("<label class=\"result\"> " + callResult + " </label>");
        } else {
            out.write("<label class=\"result\">&nbsp;</label>");
        }
        out.write("</form></div>\n");
    }

    private void writeAttributes(PrintWriter out, ObjectName objectName, MBeanInfo mbeanInfo)
            throws AttributeNotFoundException, MBeanException, InstanceNotFoundException, ReflectionException {
        if (mbeanInfo.getAttributes().length > 0) {
            for (MBeanAttributeInfo attributeInfo : mbeanInfo.getAttributes()) {
                writeAttribute(out, objectName, attributeInfo);
            }
        }
    }

    private void writeAttribute(PrintWriter out, ObjectName objectName, MBeanAttributeInfo attributeInfo)
            throws AttributeNotFoundException, MBeanException, InstanceNotFoundException, ReflectionException {
        if (!attributeInfo.isWritable()) {
            out.write("<div><label>" + attributeInfo.getName() + "</label>  <input type=\"text\" name=\"value\" value=\""
                    + mbeanServer.getAttribute(objectName, attributeInfo.getName()) + "\"  disabled=\"disabled\"/></div>");
        } else {
            out.write("<div><form><label>" + attributeInfo.getName() + "</label>  <input type=\"text\" name=\"value\" value=\""
                    + mbeanServer.getAttribute(objectName, attributeInfo.getName()) + "\"/>");
            out.write("<input type=\"hidden\" name=\"attribute\" value=\"" + attributeInfo.getName() + "\" />");
            out.write("<input type=\"hidden\" name=\"mbean\" value=\"" + objectName.getKeyProperty("type") + "\" />");
            out.write("<input type=\"submit\" name=\"submit\" value=\"save\" />");
            out.write("</form></div>\n");
        }
    }

    private void writeHeader(PrintWriter out) {
        out.write("<head>\n");
        out.write("<style>\n");
        out.write("div.mbean {\n\toverflow:hidden;\n\tdisplay: inline-block;\n\tpadding: 10px;\n\tmargin: 10px;\n\tbackground: #ccc;\n}\n");
        out.write("div.mbean label {\n\twidth:160px;\n\tdisplay:block;\n\tfloat:left;\n\ttext-align:left;\n\tfloat:left;\n}\n");
        out.write("div.mbean input {\n\tmargin-left:4px;\n\tfloat:left;\n\tfloat:left;\n}\n");
        out.write("div.mbean {\n\t-moz-box-shadow:3px 3px 5px 1px #666;\n\t-webkit-box-shadow:3px 3px 5px 1px #666;\n\tbox-shadow:3px 3px 5px 1px #666;\n}\n");
        out.write("div.mbean label.result{\n\tcolor:#0000cc;\n\ttext-align:center;\n\tbackground: #ddd;\n\t-moz-box-shadow:inset 3px 3px 5px 1px #222;\n\t-webkit-box-shadow:inset 3px 3px 5px 1px #222;\n\tbox-shadow:inset 2px 2px 4px 1px #222;\n\tpadding-top:3px;\n}\n");
        out.write("</style>\n");
        out.write("</head>\n");
    }
}