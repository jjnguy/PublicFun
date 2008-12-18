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
		<h2>Java Runtime Environment</h2>
		<h3><a href="http://www.java.com/en/download/manual.jsp" >jre-1.6.x</a></h3>
		<h2>Apache Tomcat Libraries</h2>
		<h3><a href="http://tomcat.apache.org/download-55.cgi" >jsp-api.jar</a></h3>
		<h3><a href="http://tomcat.apache.org/download-55.cgi" >servlet-api.jar</a></h3>
		<h2>Apache Commons Libraries</h2>
		<h3><a href="http://commons.apache.org/fileupload/using.html" >commons-fileupload-1.2.jar</a></h3>
		<h3><a href="http://commons.apache.org/io/description.html" >commons-io.*.jar</a></h3>
		<h2>MySQL to Java Connector</h2>
		<h3><a href="http://dev.mysql.com/downloads/connector/j/5.1.html" >mysql-connector-java.jar</a></h3>
		<h1></h1>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>