<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="infoExpert.SongData"%>
<%@page import="controller.Controller"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="styles/mainstylesheet.css" />
		<title>jTunes - Search Results</title>
	</head>
	<body>
		<%
		String term  = (String)request.getParameter("searchTerm");
		List<SongData> data = Controller.getController().simpleSearch(term);
		%>
		<%= term %>
	</body>
</html>
