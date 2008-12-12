<%@page import="java.io.FileInputStream"%>
<%@page import="actual.DownloadSong"%>
<%@page import="controller.Controller"%>

<html>
	<head>
		<title>Download Song</title>
	</head>
	<body>
	<%
		try {
			// set the http content type to mp3 
			response.setContentType("audio/mpeg");
	   
			String title = (String)request.getParameter("title");
			response.setHeader("Content-Disposition", "Attachment; Filename=\"" 
			+ title + ".mp3\"");
			FileInputStream fileInputStream;
	   
			DownloadSong down = Controller.getController();
			fileInputStream = down.downloadSong((String)request.getParameter("fileName"));
			
			int i;
			while ((i=fileInputStream.read())!=-1) {
		out.write(i);
	   		}
	   		fileInputStream.close();
	   		out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	%>
	</body>
</html>