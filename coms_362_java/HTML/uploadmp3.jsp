<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="id3TagStuff.ID3v2_XTag"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>
<%@page import="id3TagStuff.frames.ID3v2_XFrame"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileOutputStream"%><html>
<%@page import="javax.swing.*" %>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
	</head>
	<body>
		<%
			FileItemFactory f = new DiskFileItemFactory();
			ServletFileUpload serv = new ServletFileUpload(f);
			List<FileItem> items = serv.parseRequest(request);
			File file;
			for (FileItem fItem: items){
				file = new File(fItem.getName());
				InputStream s = fItem.getInputStream();
				FileOutputStream fOs = new FileOutputStream(file);
				while (s.available() > 0){
					fOs.write(s.read());
				}
			}
			
			String loc = request.getParameter("fileLoc");
			ID3v2_XTag fileTag = new ID3v2_XTag(new File(loc));
			List<ID3v2_XFrame> frames; 
			frames = fileTag.getAllFrames();
			for (ID3v2_XFrame frame: frames) {
				frame.getEnglishTagDescription();
				frame.getData();
			}
		%>
		<% if (false) { %>
		<h1><%= loc %></h1>
		<%} else { %>
		<h2><%= loc %></h2>
		<% } %>
	</body>
</html>
