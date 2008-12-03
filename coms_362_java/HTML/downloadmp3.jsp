<%@page import="java.io.FileInputStream"%>
<%@page import="controller.DownloadSong"%>
<%@page import="controller.Controller"%>



<html>
	<head>
	</head>
	<body>
<% try {

   

   // set the http content type to mp3 
   response.setContentType("audio/mpeg");
   
   	String title = (String)request.getParameter("title");
	response.setHeader("Content-Disposition", "Attachment; Filename=\"" + title + ".mp3\"");
	FileInputStream fileInputStream;
   
	DownloadSong down = Controller.getController();
	
	
	fileInputStream = down.downloadSong((String)request.getParameter("fileName"));
   
   int i;
   while ((i=fileInputStream.read())!=-1)
   {
      out.write(i);
   }
   fileInputStream.close();
   out.close();
   }catch(Exception e) // file IO errors
   {
   e.printStackTrace();
}
%>

</body>
</html>