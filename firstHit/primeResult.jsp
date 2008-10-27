
<html>
	<%
		int num = Integer.parseInt(request.getParameter("number"));
		boolean isPrime = true;
		int lim = (int)Math.sqrt(num) + 1;
		if (num == 2){
			
		}else if (num % 2 == 0){
			isPrime = false;
		}  else {
			for (int i = 3; i < lim; i+=2){
				if (num % i == 0) {
					isPrime = false;
					break;
				}
			}
		}
	%>
	<head>
		<title><%= "Is " + num + " Prime"%></title>
		<link rel="stylesheet" type="text/css" href="style.css" />

	</head>
	<body>
		<div class="mainTextDisplay">
			<h1><%= num + " is " + (isPrime ? "": "not ") + "prime." %></h1>
			<a href="primePage.html">More?</a>
		</div>
	</body>
</html>
