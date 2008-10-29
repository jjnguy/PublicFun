<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<%@page import="javax.swing.*" %>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
	</head>
	<body>
	<%
		String loc = request.getParameter("fileLoc");
	%>
	<% if (true) { %>
	<h1><%= loc %></h1>
	<%} else { %>
	<h2><%= loc %></h2>
	<% } %>
	</body>
</html>
