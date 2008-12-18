<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - Individual Efforts</title>
	</head>
	<body>
		<h1>Team Members</h1>
		<h2>Ben Petersen</h2>
		Put shit here
		<h2>Shaun Brockhof</h2>
		Put shit here
		<h2>Justin Nelson</h2>
		I was responsible for most of the User-Interface.  The JSPs controlling uploading 
		songs, logging in, searching, displaying search results, streaming, and deleting songs.
		I also wrote the API for creating ID3 tags and extracting the metadata from a song.
		Besides that, I did some little things.  I wrote the javascript validating some input 
		and alerting the user when things were deleted or not.  
		I also came up with most of the design for the website.  The css files and JSP layouts
		are all based off my designs.
		<%= HTMLFooter.getFooter() %>
	</body>
</html>