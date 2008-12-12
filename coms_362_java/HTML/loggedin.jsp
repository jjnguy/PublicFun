<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="controller.Controller"%>
<%@page import="java.util.Arrays"%>
<%@page import="util.Util"%>
<%@page import="webInterface.HTMLFooter"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Logged In - jTunes</title>
	</head>
	<body>
		<%
		boolean loggedin = false;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Controller c = Controller.getController();
		byte[] passowrd = c.getHashedPassword(username);
		if (Arrays.equals(passowrd, Util.getHashedBytes(password.getBytes()))){
			loggedin = true;
		}
		if (loggedin){
			Cookie co = new Cookie(Controller.USERNAME_COOKIENAME, username);
			co.setMaxAge(60 * 30);
			response.addCookie(co);
		}
		%>
		<% if (loggedin) { %>
		<div>
			Your Login was successful
		</div>
		<%} else { %>
		<div>
			Your Login <h2>FAILD</h2>
		</div>
		<% } %>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>