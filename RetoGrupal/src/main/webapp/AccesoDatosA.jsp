<%@page import="com.retogrupal.enitites.Residuo"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	ArrayList<String> encabezado=(ArrayList)getServletContext().getAttribute("encabezado"); 
	ArrayList<Residuo> residuos=(ArrayList)getServletContext().getAttribute("residuos"); 
%>
<html>
<head>
<meta charset="UTF-8">
<title>Acceso a los Datos</title>
</head>
<body>
<h1>DATOS</h1>
<table border='1'>
	<tr>
		<%for(String enc:encabezado){ %>
			<td><%= enc %></td>
		<%} %>
	</tr>
	<%for(Residuo res:residuos){ %>
		<tr>
			<td><%=res.getMes() %></td>
			<td><%=res.getResiduo() %></td>
			<td><%=res.getModalidad() %></td>
			<td><%=res.getCantidad() %></td>
		</tr>
	<%}%>
</table>
</body>
</html>