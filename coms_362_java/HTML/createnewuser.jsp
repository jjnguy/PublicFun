<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="webInterface.HTMLFooter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<script type="text/javascript">
			function validate(thisForm) {
				with (thisForm){ 
					if (username.value == null || username.value == "") {
						alert('Please don\'t leave any fields blank');
						return false;
					} else if (password.value == null || password.value == "") {
						alert('Please don\'t leave any fields blank');
						return false;
					} else if (password2.value == null || password2.value == "") {
						alert('Please don\'t leave any fields blank');
						return false;
					}
				}
				return true;
			}
		</script>
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
		if(error != null){ %>
			<h3><%=error%></h3>
		<% } %>
		<table class="center" border="0" >
			<tr>
				<td width="35%" ></td>
				<td style="padding: 0">
					<a href="mainMenu.jsp">
						<img class="picLink" src="../images/iTunesLogo.PNG" title="Online-iTunes Logo" />
					</a>
				</td>
				<td><h2>Create New User</h2></td>
				<td width="35%" ></td>
			</tr>
		</table>
		<form class="center createuser plainText" method="get" action="loggedin.jsp" enctype="multipart/form-data" onsubmit="return validate(this)">
			*Username<input type="text" name="username" /><br /><br />
			*Password<input type="password" name="password" /><br /><br />
			*Confirm Password<input type="password" name="password2" /><br /><br />
			<input class="button" type="submit" value="Submit" title="hi" />
		</form>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
