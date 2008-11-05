<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="styles/mainstylesheet.css" />
		<title>Online jTunes - Main Menu</title>
	</head>
	<body>
		<div class="menu">
			<ul>
				<li>
					<a class="button" href="uploadmp3.jsp" >Upload Music to the Collection</a>
				</li>
				<li>
					<a class="button" href="" >View Music Collection</a>
				</li>
				<li>
					<a class="button" href="http://www.youtube.com/user/YTRickRollsYou">See a Silly Video</a>
				</li>
			</ul>
		</div>
		<div class="group center">
			<table class="mainMenuHeaderTable">
				<tr>
					<td style="padding: 0">
						<img src="images/iTunesLogo.PNG" title="Online-iTunes Logo">
					</td>
					<td>
						<h1>Welcome to Online iTunes</h1>
					</td>
					<td width="27%" ></td>
				</tr>
			</table>
			<div class="mainMenuContent">
				<h3>What is Online iTunes?</h3>
				Online iTunes is what it sounds like.<br>
				It is a online music library that you can access from anywhere.<br>
				It allows users to add songs to the online library, sort the songs by many attributes,
				stream the songs straight from the server, and download the songs to the machine they are using currently.
				<br><br>
				<h3>Getting Started</h3>
				To begin, simply upload some songs to the library.  Then feel free to view, sort, and stream the songs from anywhere.
			</div>
		</div>
		<div style="margin-top: 80px"></div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
