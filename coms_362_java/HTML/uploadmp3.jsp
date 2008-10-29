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

<%@page import="id3TagStuff.id3Data.ID3_Picture"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
	</head>
	<body>
		<%
			DiskFileItemFactory f = new DiskFileItemFactory();
			File filx = new File("files");
			// f.setRepository(filx);
			ServletFileUpload serv = new ServletFileUpload(f);
			List<FileItem> items = serv.parseRequest(request);
			File file = null;
			String fullPath = "fail";
			String pictureSaveDir = "C:/pics/";
			{File existTest = new File(pictureSaveDir);
			if (!existTest.exists()){
				existTest.mkdirs();
			}}
			String nameOnly = null;
			for (FileItem fItem: items){
				fullPath = fItem.getName();
				String firstPart = "C:/uploads2/";
				File existTest = new File(firstPart);
				if (!existTest.exists()){
					existTest.mkdirs();
				}
				int sepIdx = fullPath.indexOf(File.separator);
				if (sepIdx == -1){
					nameOnly = fullPath;
				} else {
					nameOnly = fullPath.substring(fullPath.lastIndexOf(File.separatorChar));
				}
				file = new File(firstPart + nameOnly);
				InputStream s = fItem.getInputStream();
				FileOutputStream fOs = new FileOutputStream(file);
				while (s.available() > 0){
					fOs.write(s.read());
				}
				fOs.close();
				s.close();
			}
			
			ID3v2_XTag fileTag = new ID3v2_XTag(file);
			List<ID3v2_XFrame> frames; 
			frames = fileTag.getAllFrames();
			String html = "";
			for (ID3v2_XFrame frame: frames) {
				html += frame.getFrameType() + "<br>";
				html += frame.getData() + "<br>";
				html += "<br>";
				if (frame.getFrameType().matches("APIC|PIC")){
					ID3_Picture pic = (ID3_Picture)frame.getData();
					String picLoc = pictureSaveDir + nameOnly + ".png";
					System.out.println("Saving pic in location: " + picLoc);
					File picSaveFile = new File(picLoc);
					System.out.println("Created the file obj");
					pic.getType();
					pic.saveAs(picSaveFile);
					html += "<img src=\"" + picLoc + "\">";
				}
			}
		%>
		<%= html %>
	</body>
</html>
