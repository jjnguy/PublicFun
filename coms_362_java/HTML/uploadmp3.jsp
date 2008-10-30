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
<%@page import="java.io.FileOutputStream"%>
<%@page import="javax.swing.*" %>

<%@page import="id3TagStuff.id3Data.ID3_Picture"%>
<%@page import="webInterface.FileUploadContainer"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
	</head>
	<body>
		<%
			FileUploadContainer upload = new FileUploadContainer(request);
			List<ID3v2_XTag> tags = upload.getListOfTags();
			String html = "";
			for(ID3v2_XTag tag: tags){
				List<ID3v2_XFrame> frames; 
				frames = tag.getAllFrames();
				for (ID3v2_XFrame frame: frames) {
					html += frame.getFrameType() + "<br>";
					html += frame.getData() + "<br>";
					html += "<br>";
					/*if (frame.getFrameType().matches("APIC|PIC")){
						ID3_Picture pic = (ID3_Picture)frame.getData();
						String picLoc = pictureSaveDir + nameOnly + ".png";
						System.out.println("Saving pic in location: " + picLoc);
						File picSaveFile = new File(picLoc);
						System.out.println("Created the file obj");
						pic.getType();
						pic.saveAs(picSaveFile);
						html += "<img src=\"localhost/mp3tools/" + picLoc + "\"></img>";
					}*/
				}
				html += "<br><br><br>";
			}
		%>
		<%= html %>
	</body>
</html>
