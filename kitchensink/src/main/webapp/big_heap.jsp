<%@ page import="java.lang.*" %>        
<html>      
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<head>
<title>Big Heap</title>
</head>
<body>
<h2>Big Heap</h2>
Time : <%=new java.util.Date()%>&nbsp<hr><p>

<%
/*
try{
	Thread.sleep(1000*1000);
}catch(Exception e){}
*/
for(int i=0;i<10000;i++){
	try{
		Thread.sleep(1000);
	}catch(Exception e){}

	Object[] obj = new Object[10000000];
	session.setAttribute("obj"+i, obj);

}

%>
</body></html>
