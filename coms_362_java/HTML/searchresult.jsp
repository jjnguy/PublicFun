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
		List<SongData> data;
		boolean braodSearch = term != null;
		// if its an advanced search
		if (!braodSearch){
			artist = (String) request.getParameter("artistName");
			if (artist.trim().equals("")) artist = null;
			title = (String) request.getParameter("songTitle");
			if (title.trim().equals("")) title = null;
			album = (String) request.getParameter("albumTitle");
			if (album.trim().equals("")) album = null;
			data = Controller.getController().advancedSearch(artist,title, album, false);
		}else{
			data = Controller.getController().simpleSearch(term);
		}
		%>
		<% for (SongData song: data) {%>
			<%= String.format("Title: %s, Artist: %s, Album: %s",song.getTitle(), song.getPerformer(0), song.getAlbum()) %><br />
		<%} %>
	</body>
</html>
