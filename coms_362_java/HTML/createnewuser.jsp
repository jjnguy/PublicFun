<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="webInterface.HTMLFooter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Create New User - jTunes</title>
	</head>
<body>
	<%
	String error = request.getParameter("error");	//output error if there is one
	if(error != null){
		%>
		<h3><%=error%><h3><br />
		<%
	}
	%>
	<div class="center plainText" title="Hi, I'm tool-tip">
		<table class="center" border="0" >
			<tr>
				<td width="40%" ></td>
				<td style="padding: 0">
					<a href="mainMenu.jsp">
						<img class="picLink" src="../images/iTunesLogo.PNG" title="Online-iTunes Logo" />
					</a>
				</td>
				<td width="35%" ></td>
			</tr>
			<tr>
				<div class="createuser plainText">
					<form method="get" action="loggedin.jsp" enctype="multipart/form-data" >
						<h2>Create New User</h2><br /><br />
						*Username<input type="text" name="username" /><br /><br />
						*Password<input type="password" name="password" /><br /><br />
						*Confirm Password<input type="password" name="password2" /><br /><br />
						<input class="button" type="submit" value="Submit" title="hi" />
					</form>
				</div>
			</tr>
				
		</table>
	</div>

	<%= HTMLFooter.getFooter() %>

</body>
</html>
