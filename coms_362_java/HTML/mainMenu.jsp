<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%>
<%@page import="controller.Controller"%>
<%@page import="util.Util"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Online jTunes - Main Menu</title>
		<% 
		String message = (String)request.getParameter("message");
		if (message == null)message = "";
		%>
	</head>
	<body onload="<%= message %>">
		<%
		String username = Util.findUsername(request.getCookies());
		%>
		<% if (username != null){ %>
		<div class="menu">
			<ul>
				<li>
					<a class="listButton" href="uploadmp3.jsp" >Upload Music to the Collection</a>
				</li>
				<li>
					<a class="listButton" href="searchcollection.jsp">Search Music Collection</a>
				</li>
				<li>
					<a class="listButton" href="searchresult.jsp?broadSearchTerm=" >View Music Collection</a>
				</li>
				<%if(username.equals("admin")) { %>
					<li>
						<a class="listButton" href="manageusers.jsp" >Manage Users</a>
					</li>
				<% } %>
				<li>
					<a class="listButton" href="logout.jsp" >Logout</a>
				</li>
			</ul>
		</div>
		<% } else { %>
		<div class="login group plainText" >
			<h2>Login</h2>
			<form class="loginForm" method="get" action="loggedin.jsp" enctype="multipart/form-data" >
				Username: <br />
				<input type="text" name="username" /><br />
				Password: <br />
				<input type="password" name="password" /><br />
				<input type="submit" class="smallLoginButton"  style="margin-bottom: 10px" value="Log On" />
			</form>
			<a class="button" href="createnewuser.jsp" >Create New User</a><br />
		</div>
		<% } %>
		<div class="group center">
			<table class="mainMenuHeaderTable">
				<tr>
					<td style="padding: 0">
						<a href="mainMenu.jsp">
							<img class="picLink" src="../images/iTunesLogo.PNG" title="Online-iTunes Logo" />
						</a>
					</td>
					<td>
						<h1>Welcome to Online jTunes</h1>
					</td>
					<td class="username plainText" width="40%" ><%= username==null?"":"Logged in as " + username %></td>
				</tr>
			</table>
			<div class="mainMenuContent">
				<h3>What is Online jTunes?</h3>
				Online jTunes is exactly what it sounds like.<br class="noPad" />
				It is a online music library that you can access from anywhere.<br class="noPad" />
				It allows users to add songs to the online library, sort the songs by many attributes,
				stream the songs straight from the server, and download the songs to the machine they are using currently.
				<br class="nopad" /><br class="nopad" />
				<h3>Getting Started</h3>
				To begin, create an account.  It is very simple. <br />
				Then, simply upload some songs to the library.  Feel free to view, sort, and stream the songs from anywhere.<br/>
				To see some documentation about the development of this website click <a href="documentation/finalPaper.jsp">here</a>.
			</div>
		</div>
		<div style="margin-top: 80px"></div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
