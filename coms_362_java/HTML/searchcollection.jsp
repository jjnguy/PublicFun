<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>jTunes - Search Song Collection</title>
		<link rel="stylesheet" type="text/css" href="styles/mainstylesheet.css" />
	</head>
	<body>
		<div class="center plainText" title="Hi, I'm tool-tip">
			<table class="center" border="0" >
				<tr>
					<td width="35%" ></td>
					<td style="padding: 0">
						<img src="images/iTunesLogo.PNG" title="Online-iTunes Logo">
					</td>
					<td>
						<h2>Search for Songs!</h2>
					</td>
					<td width="35%" ></td>
				</tr>
			</table>
		</div>
		<div class="center plainText">
			<form method="get" action="searchresult.jsp" enctype="multipart/form-data" >
				<input type="text" name="searchTerm" >
				<input type="submit" value="Search" title="hi">
			</form>
		</div>
	</body>
</html>
