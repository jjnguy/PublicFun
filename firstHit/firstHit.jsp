
<html>
	<%
		String aName = request.getParameter("aName");
		if (aName.length() == 0){
			aName = "Why didn't you enter any text???";
		}
		// List<Integer> l = new ArrayList<Integer>();
	%>
	<head>
		<title>A test JSP - <%= aName %></title>
		<link rel="stylesheet" type="text/css" href="style.css" />

	</head>
	<body>
		<div class="mainTextDisplay">
			<h1><%= aName %></h1>
		</div>
		<div class="surprisePic">
			<%
				String imgToDisp = "";
				String currentUser = System.getProperty("user.name");
				if (aName.equalsIgnoreCase(currentUser)){
					imgToDisp = "<img src=\"images/myFace.jpg\"></img>";
				}
			%>
			<%= imgToDisp %>
		</div>
	</body>
</html>
