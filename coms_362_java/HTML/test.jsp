
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>

<% try {

   String filename = "C:\\test.mp3";

   // set the http content type to "APPLICATION/OCTET-STREAM
   response.setContentType("audio/mpeg");

   // initialize the http content-disposition header to
   // indicate a file attachment with the default filename
   // "myFile.txt"
   String disHeader = "Attachment; Filename=\"myFile.mp3\"";
   response.setHeader("Content-Disposition", disHeader);

   // transfer the file byte-by-byte to the response object
   File fileToDownload = new File(filename);
   FileInputStream fileInputStream = new
      FileInputStream(fileToDownload);
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