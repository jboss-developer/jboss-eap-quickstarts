<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>

<head>
<title>Spring MVC Starter Application</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/static/resources/css/screen.css"/>" />
</head>

<body>
	<div id="container">
		<div class="dualbrand">
			<img src="<c:url value="/static/resources/gfx/dualbrand_logo.png"/>" />
		</div>
		<div id="content">
			<c:out value="${error}"></c:out>
		</div>
		<div id="footer">
			<p>
				This project was generated from a Maven archetype from JBoss.<br />
			</p>
		</div>
	</div>
</body>
</html>
