<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.jboss.as.quickstarts.logging.LoggingExample" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Logging Quickstart</title>
</head>
<body>
<%
LoggingExample.logDebug();
LoggingExample.logError();
LoggingExample.logFatal();
LoggingExample.logInfo();
LoggingExample.logTrace();
LoggingExample.logWarn();
%>
Log Messages have been written.  Please check your console as well as <i>/JBoss_Home</i>/standalone/log for messages
</body>
</html>