<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="shortcut icon" href="favicon.ico" />
<title>PING Objects Migration</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<header>
		<h2>PING Objects Migration</h2>
	</header>
	<div id="sidebar">
		<div id="nav">
			<ul>
				<li class=""><a href="#result">Results</a></li>
			</ul>
		</div>
		<div class="copyright">
			Copyright © 2020 <br> Optum Technology
		</div>
	</div>

	<div class="container">

		<div id="#result">
			<table cellpadding="1" cellspacing="1" border="1" bordercolor="gray">
				<tr>
					<td><%=request.getAttribute("samlresult")%></td>
				</tr>
			</table>
				<br>
			<a href="index.jsp">Home</a>
		</div>
	</div>
</body>
</html>