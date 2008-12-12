<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="controller.Controller"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>jTunes - Logout</title>
<link rel="stylesheet" type="text/css"
	href="../styles/mainstylesheet.css" />
<link rel="icon" href="../images/favicon.png" type="image/png" />
<link rel="shortcut icon" href="../favicon.ico" />
</head>
<body>
<%
	Cookie[] cookies = request.getCookies();
	for (Cookie cook : cookies) {
		if (cook.getName().equals(Controller.USERNAME_COOKIENAME)){
			cook.setMaxAge(1);
			break;
		}
	}
	response.sendRedirect("mainMenu.jsp");
%>
</body>
</html>