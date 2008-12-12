<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="controller.interfaces.UploadSong"%>
<%@page import="controller.Controller"%>

<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItem"%>
<%@page import="webInterface.HTMLFooter"%>
<%@page import="util.Util"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Upload Results</title>
		<link rel="stylesheet" type="text/css" href="../styles/mainstylesheet.css" />
		<link rel="icon" href="../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../favicon.ico" />
	</head>
	<body>
		<%
		String username = Util.findUsername(request.getCookies());
		boolean iTunesUpload = request.getParameter("iTunes") != null;
			String message = null;
			try {
		DiskFileItemFactory f = new DiskFileItemFactory();
		ServletFileUpload serv = new ServletFileUpload(f);
		FileItem file = (FileItem)serv.parseRequest(request).get(0);
		
		UploadSong up = Controller.getController();
		message = up.uploadSong(file.getInputStream());
			} catch (NullPointerException e) {
		message = "There was an error uploading your song.  Sorry, try again later.";
			}
			message = "Your song was successfully uploaded to the music collection!";
		%>
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
						<h2>The Results of Your Upload</h2>
					</td>
					<td width="35%" ></td>
				</tr>
			</table>
		</div>
		<div class="center">
			<span class="plainText" ><%= message %></span>
		</div>
		<div class="center" style="margin-top:20px" >
			<a class="button" href="uploadmp3.jsp">UploadAnother?</a>
		</div>
		<%= HTMLFooter.getFooter() %>
	</body>
</html>