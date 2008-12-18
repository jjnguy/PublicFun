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
		<h1>External Influences</h1>
		While designing this site, since it was our first time, we took a lot of our ideas
		from other websites and programs.
		<h2>iTunes</h2>
		iTunes is our namesake as well as where we got out logo from.  It is also where the 
		idea of our website came from.  We basically decided that we wanted an online
		iTunes.
		<h2>Stack Overflow</h2>
		<a href="http://stackoverflow.com">StackOverflow.com</a>
		The design for the website was largly influenced from this website.  The style of the
		buttons and the simple blocky layout all came from this website.  I also checked 
		out the source of their site and used it to help lay out out site.
		<h2>Mp3 Tunes</h2>
		<a href="http://mp3tunes.com/">Mp3Tunes.com</a>
		While developing this site we came across this website that has almost identical
		use as ours.  It, of course, is way better.  But we took a few ideas from there
		to help make our site better.
		<h1>References</h1>
		Being rookies to this web stuff, we needed a lot of help.  Below are a few of the 
		sites that especially helped us on our website development journey.
		<h2>W3 Schools</h2>
		This is prety much the definitive web builders reference.  Nuff said.
		<h2>MY SQL Reference Manual</h2>
		http://dev.mysql.com/doc/refman/5.0/en/tutorial.html
		Shaun had done a bit of database work in his 363 class.  However, we were doing more
		complex things than he was used to.  The reference manual was an invaluable reference.
		<h2>Stack Overflow</h2>
		This is my (Justin's) favorite site lately.  I found a ton of answers regarding web design
		and JSP and css stuff here.
		<h2>Google</h2>
		When in doubt Google it.  We used too many examples from Google to list them all.
		<%= HTMLFooter.getFooter() %>
	</body>
</html>