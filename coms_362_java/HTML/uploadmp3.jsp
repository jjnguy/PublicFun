<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="webInterface.HTMLFooter"%>
<html>
	<head>
		<title>Upload a file to the server</title>
		<link rel="stylesheet" type="text/css" href="mainstylesheet.css" />
	</head>
	<body>
		<div class="group wrapper">
			<div class="center plainText" title="Hi, I'm tool-tip">
				<table class="center" border="0" >
					<tr>
						<td width="15%" ></td>
						<td style="padding: 0">
							<img src="images/iTunesLogo.PNG" title="Online-iTunes Logo">
						</td>
						<td>
							<h1>Welcome to the Online-iTunes</h1>
							<h2>Find an MP3 file that you would like to upload to our servers.</h2>
						</td>
						<td width="15%" ></td>
					</tr>
				</table>
			</div>
			<div class="input-form center" title="MP3 File location">
				<form method="post" action="uploadresult.jsp" method="get" enctype="multipart/form-data">
					<input type="file" name="fileLoc" size="40">
					<br>
					<input type="submit" value="Submit" title="Submit File for Upload">
				</form>
			</div>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
