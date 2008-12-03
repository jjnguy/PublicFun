<%@page import="controller.Controller"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Download Song</title>
	</head>
	<body>
		<script language="JavaScript" src="audio/audio-player.js"></script>
		<object type="application/x-shockwave-flash" data="audio/player.swf" id="audioplayer1" height="24" width="290">
			<param name="movie" value="audio/player.swf">
			<param name="FlashVars" value="playerID=1&amp;soundFile=<%=request.getParameter("fileURL") %>">
			<param name="quality" value="high">
			<param name="menu" value="false">
			<param name="wmode" value="transparent">
		</object> 
	</body>
</html>

