<%@ page import="java.lang.*" %>        
<html>      
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<head>
<title>infinite loop</title>
</head>
<body>
<h2>infinite loop</h2>

Time : <%=new java.util.Date()%>&nbsp<hr><p>

<pre>
Request Method: <%= request.getMethod() %>
Request URI: <%= request.getRequestURI() %>
Request Protocol: <%= request.getProtocol() %>
Servlet Path: <%= request.getServletPath() %>
Path Info: <%= request.getPathInfo() %>
Path Translated: <%= request.getPathTranslated() %>
Query String: <%= request.getQueryString() %>
Content Length: <%= request.getContentLength() %>
Content Type: <%= request.getContentType() %>
Server Name: <%= request.getServerName() %>
Server Port: <%= request.getServerPort() %>
Remote User: <%= request.getRemoteUser() %>
Remote Address: <%= request.getRemoteAddr() %>
Remote Host: <%= request.getRemoteHost() %>
Authorization Scheme: <%= request.getAuthType() %>

WL-Proxy-Client-IP: <%= request.getHeader("WL-Proxy-Client-IP") %>
Proxy-Client-IP: <%= request.getHeader("Proxy-Client-IP") %>
X-Forwarded-For: <%= request.getHeader("X-Forwarded-For") %>

</pre>

<%
 int i=10;
 while(i>1)
 {

 }
         
%>
</body></html>
