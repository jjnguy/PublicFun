<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../../styles/mainstylesheet.css" />
		<link rel="stylesheet" type="text/css" href="../../styles/buttons.css" />
		<link rel="icon" href="../../images/favicon.png" type="image/png" />
		<link rel="shortcut icon" href="../../favicon.ico" />
		<title>jTunes - Description of Design</title>
	</head>
	<body>
		<p>Our website tries to follow the <a href="http://en.wikipedia.org/wiki/Model-view-controller">MVC</a> 
		design pattern.  Our JSP's do as little work as we could.  The Controller class handles all requests
		from the JSP's.  The only class visible from the JSP's view point is the Controller.</p>
		<p>The controller does all of the delegating.  Given any request, it finds the approrieate class
		and executes the proper method.</p>
		<p>The controller then takes the return value from the model and passes it to the JSP's to to the rendering</p>
		
	</body>
</html>