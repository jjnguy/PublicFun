package org.apache.jsp.HTML;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import id3TagStuff.ID3v2_XTag;
import java.io.File;
import java.util.List;
import id3TagStuff.frames.ID3v2_XFrame;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.io.InputStream;
import java.io.FileOutputStream;
import javax.swing.*;
import id3TagStuff.id3Data.ID3_Picture;

public final class uploadmp3_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.List _jspx_dependants;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html; charset=ISO-8859-1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\r\n");
      out.write("\t\t<title>Upload Results</title>\r\n");
      out.write("\t</head>\r\n");
      out.write("\t<body>\r\n");
      out.write("\t\t");

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
					html += "<img src=\"" + picLoc + "\"></img>";
				}
			}
		
      out.write("\r\n");
      out.write("\t\t");
      out.print( html );
      out.write("\r\n");
      out.write("\t</body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
