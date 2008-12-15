<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="controller.Controller"%>
<%@page import="webInterface.HTMLFooter"%>
<%@page import="databaseAccess.Database"%>
<%@page import="java.util.Collection"%>


<%@page import="util.Util"%><html>
	<head>
		<%
		String username = Util.findUsername(request.getCookies());
		String delete = request.getParameter("delete");
		String nameToDelete = request.getParameter("nametodelete");
		String message = "";
		if (delete != null){
			Controller c = Controller.getController();
			message = c.deleteUser(nameToDelete, username);
			message = "alert('" + message + "')";
		}
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<title>jTunes - Manage Users</title>
	</head>
	<body onload="<%= message %>">
		<div class="center plainText" title="Hi, I'm tool-tip">
			<table class="center" border="0" >
				<tr>
					<td width="37%" ></td>
					<td style="padding: 0">
						<a href="mainMenu.jsp">
							<img class="picLink" src="../images/iTunesLogo.PNG" title="Online-iTunes Logo" />
						</a>
					</td>
					<td>
						<h2>Manage Users</h2>
					</td>
					<td width="37%" ></td>
				</tr>
			</table>
		</div>
		<%
		
		if(username.equals("admin")){
			List<String> users = null;
			users = Controller.getController().getAllUsers();
			%>
			<div class="allResults">
			<div style="float:left;">
			<% 
			if (users != null && users.size() != 0)
				for (String user: users) {%>
					<div class="searchResult" >
						<div>
							<%=user%>
						</div>
						<form style="margin-left: 40px" method="get" action="manageusers.jsp" enctype="multipart/form-data">
							<input type="hidden" name="delete" value="gtfo" />
							<input type="hidden" name="nametodelete" value="<%=user%>" />
							<input type="submit" class="smallButton" name="nameToDelete" value="Delete User" />
						</form>
					</div>
				<%} 
			else { %>
			<center style="font-weight:bold; font-size:large;">Your search did not return any results.</center>
			<%}%>
			</div>
		<%}
		else{
			response.sendRedirect("mainMenu.jsp");
		}
		%>
			
		<%= HTMLFooter.getFooter() %>
		</div>
		
	</body>
</html>
