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
		<title>jTunes - Interval Breakdown</title>
	</head>
	<body>
		<div class="finalPaperFull">
			<h1>Interval Breakdown</h1>
			<p class="plainText" style="margin-left: 10px">This page was created to provide an overview of what portions of the project
			were done in each interval.</p>
			<h2>Interval 1</h2>
			<div class="overview">
				<ul>
					<li>-Not a use case...but setting up the Java, MySQL, Tomcat server/environment</li>
					<li>-Uploading files to the jTunes server (file and database info)</li>
					<li>-View information about files</li>
				</ul>
			</div>
			<h2>Interval 2</h2>
			<div class="overview">
				<ul>
					<li>-Search for files by various criteria</li>
					<li>-Sort files by various criteria</li>
					<li>-Download music files</li>
					<li>-Stream music files</li>
				</ul>
			</div>
			<h2>Interval 3</h2>
			<div class="overview">
				<ul>
					<li>-Create individual user accounts (and ownership rights for files)</li>
					<li>-Users can delete songs that they have ownership of (i.e. they uploaded it)</li>
					<li>-Create admin account with admin rights</li>
					<li>----View all music files regardless of ownership</li>
					<li>----Delete any music files regardless of ownership</li>
					<li>----Delete users from the system</li>
				</ul>
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>