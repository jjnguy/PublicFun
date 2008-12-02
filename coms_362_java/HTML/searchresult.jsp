<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="infoExpert.SongData"%>
<%@page import="controller.Controller"%>
<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>jTunes - Search Results</title>
	</head>
	<body>
		<%
		String term  = (String) request.getParameter("broadSearchTerm");
		String artist = null, title = null, album = null;
		// if its an advanced search
		if (term == null){
			artist = (String) request.getParameter("artistName");
			title = (String) request.getParameter("songTitle");
			album = (String) request.getParameter("albumTitle");
		}
		List<SongData> data = Controller.getController().simpleSearch(term);
		%>
		Broad Search: <%= term %><br />
		<%= data.get(0).getTitle() %>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
