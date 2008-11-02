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
			<div style="float: left; padding-top: 3cm">
				<ul style="padding: 0">
					<li style="margin-top: .4cm">
						<a class="button" href="uploadmp3.jsp">Upload Music to the Collection</a>
					</li>
					<li style="margin-top: .4cm">
						<a class="button" href="">View Music Collection</a>
					</li>
					<li style="margin-top: .4cm">
						<a class="button" href="http://www.youtube.com/user/YTRickRollsYou">See a Silly Video</a>
					</li>
				</ul>
			</div>
			<div class="group center">
				<table class="center" border="0" >
					<tr>
						<td style="padding: 0">
							<img src="images/iTunesLogo.PNG" title="Online-iTunes Logo">
						</td>
						<td>
							<h1>Welcome to Online iTunes</h1>
						</td>
						<td width="27%" ></td>
					</tr>
				</table>
				<div style="text-align: left">
					This is actual content describing the website.
				</div>
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
