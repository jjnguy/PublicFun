<%@page import="controller.Controller"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="webInterface.HTMLFooter"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
		<title>Stream Song</title>
	</head>
	<body>
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
						<h2>Stream Song</h2>
					</td>
					<td width="37%" ></td>
				</tr>
			</table>
		</div>
		<script language="JavaScript" src="audio/audio-player.js"></script>
		<center>
		<object class="mp3player" type="application/x-shockwave-flash" data="audio/player.swf" id="audioplayer1" height="24" width="290">
			<param name="movie" value="audio/player.swf">
			<param name="FlashVars" value="playerID=1&amp;soundFile=<%=request.getParameter("fileURL") %>">
			<param name="quality" value="high">
			<param name="menu" value="false">
			<param name="wmode" value="transparent">
		</object> 
		</center>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>

