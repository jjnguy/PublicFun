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
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Logged In - jTunes</title>
	</head>
	<body>
		<%
		boolean loggedin = false;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		Controller c = Controller.getController();
		
		if(password2 != null){	//New user request
			if(password.equals(password2)){
				String result = c.createUser(username, Util.getHashedBytes(password.getBytes()));
				if(result.equals("User could not be created.")){
					response.sendRedirect("createnewuser.jsp?error=Username already in use!");
				}
			}
			else{
				response.sendRedirect("createnewuser.jsp?error=Passwords did not match!");
			}
		}
		
		byte[] passowrd = c.getHashedPassword(username);
		if (Arrays.equals(passowrd, Util.getHashedBytes(password.getBytes()))){
			loggedin = true;
		}
		if (loggedin){
			Cookie co = new Cookie(Controller.USERNAME_COOKIENAME, username);
			co.setMaxAge(60 * 30);
			response.addCookie(co);
		}
		response.sendRedirect("mainMenu.jsp");
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