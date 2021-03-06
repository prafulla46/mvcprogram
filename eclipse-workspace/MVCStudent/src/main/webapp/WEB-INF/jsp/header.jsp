<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		Welcome : ${sessionScope.name } your email: ${sessionScope.emailId }
		<img class="img-thumbnail" src="img/school.jpg" style="height:130px"/>
		
		<nav class="navbar navbar-expand bg-dark">
			<ul class="nav navbar-nav">
				<c:choose>
					<c:when test="${sessionScope.name eq null}">
						<li class="navbar-items"><a href="login">Login</a></li>
					</c:when>
					<c:otherwise>
						<li class="navbar-items"><a href="logout">Logout</a></li>
					</c:otherwise>
				</c:choose>
				
				<li class="navbar-items"><a href="showStudents">All Students</a></li>
				<li class="navbar-items"><a href="studentRegistration">Register Students</a></li>
				
			</ul>
		</nav>
	</div>
</body>
</html>