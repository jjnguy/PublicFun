<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - System Implementation</title>
	</head>
	<body>
		<h2>System Implementation</h2><br />
		<p>Online jTunes uses a variety of technologies to bring easy music management to the masses. This
		section will detail what these technologies are and how they are used in our project.</p><br />
		<h3>View</h3>
		<p>The front end of jTunes is controlled by JSP, HTML, and CSS code running on an Apache
		Tomcat web server that is running on a Windows XP computer.  We are also utilizing a small
		snippet of Javascript code to allow users to stream songs from the jTunes server, as well as validate user forms.</p><br />
		<h3>Model</h3>
		<p>The back end of jTunes is written almost entirely in Java code, with some SQL queries as 
		well.  Song data is stored in a MySQL database, which is located on the same Windows XP 
		computer as the web server itself.  The actual song files are stored in a folder on the
		web server, but under our own file naming scheme, for better storage and retrieval.</p>
		<p>Another notable feature of the back end code, also written in Java, is the parsing of
		ID3 v2.x tags, which have their own complicated protocol for embedding song information
		inside the mp3 files.  We use the information extracted from the ID3 v2.x tags to populate 
		the database.</p>
		
		
		<%= HTMLFooter.getFooter() %>
	</body>
</html>