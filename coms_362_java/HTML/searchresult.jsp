<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="infoExpert.SongData"%>
<%@page import="controller.Controller"%>
<%@page import="webInterface.HTMLFooter"%>
<%@page import="databaseAccess.Database"%>
<%@page import="java.util.Collection"%>


<%@page import="util.Util"%><html>
	<head>
		<%
		String username = Util.findUsername(request.getCookies());
		String delete = request.getParameter("delete");
		String fileName = request.getParameter("fileName");
		String message = "";
		if (delete != null){
			Controller c = Controller.getController();
			message = c.removeSong(fileName, username);
			message = "alert('" + message + "')";
		}
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../styles/buttons.css" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<title>jTunes - Search Results</title>
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
						<h2>Search Results</h2>
					</td>
					<td width="37%" ></td>
				</tr>
			</table>
		</div>
		<%
		String sortName = request.getParameter("sort");
		int sortTerm = Controller.SORT_BY_TITLE;
		if (sortName != null)
			sortTerm = sortName.equals("album") ? Controller.SORT_BY_ALBUM : 
				(sortName.equals("artist") ? Controller.SORT_BY_ARTIST:Controller.SORT_BY_TITLE);
		String term  = (String) request.getParameter("broadSearchTerm");
		String artist = (String) request.getParameter("artistName");
		String title = (String) request.getParameter("songTitle");
		String album = (String) request.getParameter("albumTitle");
		
		List<SongData> data = Controller.getController().search(term, title, artist, album, sortTerm, Util.findUsername(request.getCookies()));
		%>
		<div class="allResults">
			<div style="float:left;">
		<% 
		if (data != null && data.size() != 0)
		for (SongData song: data) {%>
			<div class="searchResult" >
				<div class="songInfo">
					<%= "Title: " + song.getTitle() %><br />
					<%= "Artist: " + song.getPerformer(0) %><br />
					<%= "Album: " + song.getAlbum() %><br />
				</div>
				<div class="songOptions" >
					<form style="margin-left: 40px" action="streammp3.jsp" method="get" enctype="multipart/form-data">
						<input type="hidden"  name="fileURL" value="<%= Controller.MP3_URL + song.getFileName() %>" />
						<input type="submit" class="smallResultButton" name="submit" value="Stream Song" />
					</form>
					<form style="margin-left: 40px" method="get" action="downloadmp3.jsp" enctype="multipart/form-data">
						<input type="hidden"  name="title" value="<%= song.getTitle() %>" />
						<input type="hidden"  name="fileName" value="<%= song.getFileName() %>" />
						<input type="submit" class="smallResultButton" name="fileName" value="Download Song" />
					</form>
					<form style="margin-left: 40px" method="get" action="searchresult.jsp" enctype="multipart/form-data">
						<input type="hidden"  name="delete" value="gtfo" />
						<input type="hidden"  name="fileName" value="<%= song.getFileName() %>" />
						<input type="hidden" name="broadSearchTerm" value="" >
						<input type="submit" class="smallResultButton" name="fileName" value="Delete Song" />
					</form>
				</div>
			</div>
		<%} else { %>
			<center style="font-weight:bold; font-size:large;">Your search did not return any results.</center>
		<%}%>
			</div>
			<div class="group center searchAgain plainText">
				Advanced Search<br />
				<form method="get" action="searchresult.jsp" enctype="multipart/form-data" >
					Song Title<input type="text" name="songTitle" /><br />
					Artist Name<input type="text" name="artistName" /><br />
					Album Title<input type="text" name="albumTitle" /><br />
					Sort By: <br />
					<input type="radio" name="sort"  value="song" />Song<br />
					<input type="radio" name="sort"  value="artist" />Artist<br />
					<input type="radio" name="sort"  value="album" />Album<br />
					<input class="button" type="submit" value="Search" title="hi" />
				</form>
			</div>
		<%= HTMLFooter.getFooter() %>
		</div>
	</body>
</html>
