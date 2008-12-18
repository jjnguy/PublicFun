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
		<title>jTunes - References</title>
	</head>
	<body>
		<h1>Dependencies</h1>
		<p>Our code has a few dependencies on some third party libraries.</p>
		<h2>Java Runtime Environment 1.6.x</h2>
		<h2>Apache Tomcat Libraries</h2>
		<h3>jsp-api.jar</h3>
		<h3>servlet-api.jar</h3>
		<h2>Apache Commons Libraries</h2>
		<h3>commons-fileupload-1.2.jar</h3>
		<h3>commons-io.*.jar</h3>
		<h2>MySQL to Java Connector</h2>
		<h3>mysql-connector-java.jar</h3>
		<h1></h1>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>