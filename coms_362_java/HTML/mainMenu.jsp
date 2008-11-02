<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="mainstylesheet.css" />
		<title>Online iTunes - Main Menu</title>
	</head>
	<body>
		<div class="wrapper" <%= HTMLFooter.WRAPPER_FOOTER_STYLE %>>
			<div class="plainText group" style="float: left; width: 100px">
				<ul>
					<li>Hi</li>
					<li>Bye</li>
					<li>OK</li>
				</ul>
			</div>
			<div class="group center" style="float: right">
				Lets see what happens
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
