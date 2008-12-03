<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="webInterface.HTMLFooter"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>jTunes - Search Song Collection</title>
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
	</head>
	<body>
		<div class="center plainText" title="Hi, I'm tool-tip">
			<table class="center" border="0" >
				<tr>
					<td width="35%" ></td>
					<td style="padding: 0">
						<a href="mainMenu.jsp">
							<img class="picLink" src="../images/iTunesLogo.PNG" title="Online-iTunes Logo" />
						</a>
					</td>
					<td>
						<h2>Search Your Music!</h2>
					</td>
					<td width="35%" ></td>
				</tr>
			</table>
		</div>
		<div class="center plainText">
			<form class="basicSearch" method="get" action="searchresult.jsp" enctype="multipart/form-data" >
				Basic Search:  Type a term to do a broad search <br />
				<input type="text" name="broadSearchTerm" ><br />
				Sort By: <br />
				<input type="radio" name="sort"  value="song" />Song<br />
				<input type="radio" name="sort"  value="artist" />Artist<br />
				<input type="radio" name="sort"  value="album" />Album<br />
				<input type="submit" value="Search" title="hi" style="margin-left:0">
			</form>
			<form class="advancedSearch" method="get" action="searchresult.jsp" enctype="multipart/form-data" >
				Advanced Search: Search one or More Fields<br />
				Song Title<input type="text" name="songTitle" /><br />
				Artist Name<input type="text" name="artistName" /><br />
				Album Title<input type="text" name="albumTitle" /><br />
				Sort By: <br />
				<input type="radio" name="sort"  value="song" />Song<br />
				<input type="radio" name="sort"  value="artist" />Artist<br />
				<input type="radio" name="sort"  value="album" />Album<br />
				<input type="submit" value="Search" title="hi" />
			</form>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>
