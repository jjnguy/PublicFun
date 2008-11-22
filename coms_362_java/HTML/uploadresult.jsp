<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="controller.UploadSong"%>
<%@page import="controller.Controller"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
		<link rel="stylesheet" type="text/css" href="styles/mainstylesheet.css" />
	</head>
	<body>
		<%
		String message = null;
		try {
			DiskFileItemFactory f = new DiskFileItemFactory();
			ServletFileUpload serv = new ServletFileUpload(f);
			FileItem file = (FileItem)serv.parseRequest(request).get(0);
			// TODO instance
			UploadSong up = Controller.getController();
			message = up.uploadSong(file.getInputStream());
		} catch (NullPointerException e) {
			message = "There was an error uploading your song.  Sorry, try again later.";
		}
		%>
		<div class="center plainText" title="Hi, I'm tool-tip">
			<table class="center" border="0" >
				<tr>
					<td width="35%" ></td>
					<td style="padding: 0">
						<img src="images/iTunesLogo.PNG" title="Online-iTunes Logo">
					</td>
					<td>
						<h2>The Results of Your Upload</h2>
					</td>
					<td width="35%" ></td>
				</tr>
			</table>
		</div>
		<div class="center">
			<span class="plainText" ><%= message %></span>
		</div>
	</body>
</html>