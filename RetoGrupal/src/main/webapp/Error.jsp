<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page errorPage="" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body style="text-align:center;">
<hr>
<h1 style="color: red;"><%=request.getAttribute("error") != null? (String)request.getAttribute("error") : "Error aleatorio" %></h1>
</body>
</html>