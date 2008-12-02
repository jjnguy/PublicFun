<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="iTunesDataStructures.ITunesLibFile"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.util.List"%>
<%@page import="controller.SaveSong"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItem"%>
<%@page import="controller.Controller"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
		<%
		DiskFileItemFactory f = new DiskFileItemFactory();
		ServletFileUpload serv = new ServletFileUpload(f);
		FileItem file = new DiskFileItem("iTunes", null, true, 
				request.getParameter("iTunes"), 10000, new File(Controller.MP3_PATH));
		File fileF = new File("C:/hotcrossbunns");
		SaveSong.copyStream(file.getInputStream(), new FileOutputStream(fileF));
		ITunesLibFile lib = new ITunesLibFile(fileF);
		List<File> allFiles = lib.getListOfFiles("Library");
		%>
		<%
		for(File fil: allFiles) { 
		%>
			<%= fil.getName() %>
		<%}%>
		
	</body>
</html>