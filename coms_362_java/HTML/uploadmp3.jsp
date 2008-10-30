<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="id3TagStuff.ID3v2_XTag"%>
<%@page import="java.util.List"%>
<%@page import="id3TagStuff.id3Data.ID3_Picture"%>
<%@page import="webInterface.MP3FileUploadContainer"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
	</head>
	<body>
		<%
			MP3FileUploadContainer upload = new MP3FileUploadContainer(request);
			List<ID3v2_XTag> tags = upload.getListOfTags();
			
			List<String> songData = upload.getHTMLRepresentation();
		%>
		<%= songData.get(0) %>
	</body>
</html>
