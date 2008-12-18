<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/documentationStyles.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - Individual Efforts</title>
	</head>
	<body>
		<div class="finalPaperFull">
			<h1>Team Members</h1>
			<h2>Ben Petersen</h2>
			<div class="overview">
				One of my responsibilities was taking care of file storage.  This included setting up and hosting the 
				actual server that all the music and the jTunes system resides on. I also wrote the
				classes/methods that dealt with storing, retrieving (downloading) and removing mp3 files.  
				Beyond that I wrote some of the methods in the Controller that would pass information between
				the JSP's and the Database.
			</div>
			<h2>Shaun Brockhoff</h2>
			<div class="overview">
				My main responsibility in the project was setting up the database. This included creating the
				database and designing the tables for song data and users and implementing all the methods for 
				creating search queries, song/user additions and deletions, password retrieval, etc.  Apart from
				that I also contributed to the user interface by creating the create user and manage users pages 
				(based off Justin's design).  I had a small part in other tasks like researching methods for file upload and streaming capabilities.  
			</div>
			<h2>Justin Nelson</h2>
			<div class="overview">
				I was responsible for most of the User-Interface.  The JSPs controlling uploading 
				songs, logging in, searching, displaying search results, streaming, and deleting songs.
				I also wrote the API for creating ID3 tags and extracting the metadata from a song.
				Besides that, I did some little things.  I wrote the javascript validating some input 
				and alerting the user when things were deleted or not.  
				I also came up with most of the design for the website.  The css files and JSP layouts
				are all based off my designs.
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>