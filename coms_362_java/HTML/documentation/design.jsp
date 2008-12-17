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
		<title>jTunes - Description of Design</title>
	</head>
	<body>
		<p>Our website tries to follow the <a href="http://en.wikipedia.org/wiki/Model-view-controller">MVC</a> 
		design pattern.  Our JSP's do as little work as we could.  The Controller class handles all requests
		from the JSP's.  The only class visible from the JSP's view point is the Controller.</p>
		<p>The controller does all of the delegating.  Given any request, it finds the approrieate class
		and executes the proper method.</p>
		<p>The controller then takes the return value from the model and passes it to the JSP's to to the rendering</p>
		<p>What was outlined above is basically the MVC design pattern.</p>
		<h2>Specific Class Duties</h2>
		<h3>controller.Controller</h3>
		Delegates tasks to correct actor
		<h3>databaseAccess.QueryDB</h3>
		Handles all DB stuff, formatting queries as well as executing them
		<h3>actual.DatabaseSearch</h3>
		Interface for defining search behavior of Controller
		<h3>actual.Login</h3>
		Handles all loging in and creating new users
		<h3>actual.RemoveSong</h3>
		Relays messages back to the controller based on various error types
		<h3>actual.UploadSong</h3>
		Handles the transfer of data when a user chooses to upload a song to jTunes
		<h3>file.DeleteSong</h3>
		Does the physical deletion of a song from the filesystem
		<h3>file.SaveSong</h3>
		Handles saving a song to the filesystem and maintaining the current song count.
		<h3>ID3v2_XTag</h3>
		Lots of stuff regarding the ID3 tags.
		<h3>infoExpert.SongData</h3>
		Contains all of the info about a song that we save
		<h3>util.SongDataFactory</h3>
		Creates songdata from ID3 tags
		<h3>util.Util</h3>
		Util class for byte stuff and hashing stuff
		<h3>webInterface.HTMLFooter</h3>
		A simple class for easily reproducing the footer at the bottom of the page
		<%= HTMLFooter.getFooter() %>
	</body>
</html>