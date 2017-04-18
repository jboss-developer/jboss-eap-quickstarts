<!doctype html public "-//w3c/dtd HTML 4.0//en">
<html>
<!-- Copyright (c) 1999-2001 by BEA Systems, Inc. All Rights Reserved.-->
<head>
<title>Failover Test</title>
</head>

<body bgcolor="#FFFFFF">
<p>
<font face="Helvetica">

<h2>
<font color=#DB1260>
Failover Test  Uhahahahah!!! hey!!!  Don't sleep...  it's HomeWork!!!!
</font>
</h2>
<h3>Server Name : <%=System.getProperty("server")%></h3>
<p>
This JSP shows simple principles of session management
by incrementing a counter each time a user accesses a page.

<p>

<%!
  private int totalHits = 0;
%>

<%
  session = request.getSession(true);

  Integer ival = (Integer)session.getAttribute("simplesession.counter");
  if (ival == null) 
    ival = new Integer(1);
  else 
    ival = new Integer(ival.intValue() + 1);
  session.setAttribute("simplesession.counter", ival);
  System.out.println("[SessionTest] count = " + ival );
%>

<%
  Integer cnt = (Integer)application.getAttribute("simplesession.hitcount");
  if (cnt == null)
    cnt = new Integer(1);
  else
    cnt = new Integer(cnt.intValue() + 1);
  application.setAttribute("simplesession.hitcount", cnt);
  //System.out.println("[SessionTest] count = " + ival );
%>

<table border=1 cellpadding=6><tr><td width=50% valign=top>
<font face="Helvetica">

<h3>
You have hit this page <font color=red> <%= ival %></font> time<%= (ival.intValue() == 1) ? "" : "s" %>, <br>before the session times out.
</h3>
The value in <font color=red><b>red</b></font> is stored in the HTTP session (<font face="Courier New" size=-1>javax.servlet.http.HttpSession</font>), in an object named <font face="Courier New" size=-1>simplesession.counter</font>. This object has <i>session</i> scope and its integer value is re-set to <font color=red><b>1</b></font> when you reload the page after the session has timed out.
<p>
You can change the time interval after which a session times out. For more information, see the <a href= http://jboss.org>Session Timeout</a> section under <a href= @DOCSWEBROOT/webapp/sessions.html>Using Sessions And Session Persistence in Web Applications</a>.
</font></td>

<td width=50% valign=top><font face="Helvetica">
<h3>You have hit this page a total of <font color=green> <%= cnt %></font> time<%= (cnt.intValue() == 1) ? "" : "s" %>!
</h3>	

The value in <font color=green><b>green</b></font> is stored in the
Servlet Context (<font face="Courier New" size=-1>javax.servlet.ServletContext)</font>, in an object named <font face="Courier New" size=-1>simplesession.hitcount</font>. This object
has <i>application</i> scope and its integer value is incremented each time you
reload the page.

</font>
</td>
</tr></table>

<p>

</font>
</body>
</html>
