<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="controller.Controller"%>
<%@page import="java.util.Arrays"%>
<%@page import="util.Util"%>
<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Logged In - jTunes</title>
		<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String message = "";
		if(password2 != null){	//New user request
			message = Controller.getController().createUser(username, password, password2);
			message = "alert('" + message + "')";
		}
		%>
	</head>
	<body onload="<%= message %>">
		<%		
		boolean loggedin = Controller.getController().login(username, password);
		if (loggedin){
			Cookie co = new Cookie(Controller.USERNAME_COOKIENAME, username);
			co.setMaxAge(60 * 30);
			response.addCookie(co);
		}
		response.sendRedirect("mainMenu.jsp");
		%>
	</body>
</html>