<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/documentationStyles.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - Research</title>
	</head>
	<body>
		<div class="finalPaperFull">
			<h2>Topics Researched</h2>
			<div class="overview">
				<h3>ID3 Tags:</h3>
				Not much was known about ID3 tags or other types of metadata for storing data about music.
				Tons of time was spent scouring through the specification documents at 
				<a href="http://www.id3.org/Developer_Information">this location</a>.
				<br class="noPad" /><br class="noPad" />
				<h3>JSP File Uploads and Downloads:</h3>
				We searched and found many websites that were beneficial in helping us figure out
				JSP downloads and uploads.  Here are a few of them:
				<ol>
					<li><a href="http://snippets.dzone.com/posts/show/2096">DZone Snippets</a></li>
					<li><a href="http://www.devsphere.com/mapping/docs/guide/upload.html">Dev Sphere</a></li>
					<li><a href="https://www.gopay.com.cn/resin-doc/jsp/tutorial/index.xtp">caucho</a></li>
				</ol>
				<br class="noPad" />
				<h3>General Project Setup:</h3>
				One <a href="http://www.sipages.com/jetv1.shtml">website</a> in general was very helpful in 
				getting all of the necessary things set up on our local computers to develop web applications 
				using JSP and MySQL.
				<br class="noPad" /><br class="noPad" />
				<h3>MySQL Commands:</h3>
				Shaun, our DB Guru used some info from <a href="http://www.pantz.org/software/mysql/mysqlcommands.html">Pantz.org</a>
				to figure out some of the necessary commands.
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>