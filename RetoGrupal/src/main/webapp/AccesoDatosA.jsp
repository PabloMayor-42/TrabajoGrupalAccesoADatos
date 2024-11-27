<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.retogrupal.enitites.Residuo" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Acceso Datos</title>
</head>
<%
	ArrayList<Residuo> residuos = (ArrayList<Residuo>)application.getAttribute("lista_residuos");
%>
<body>
	<table border="1">
		<tr>
			<td>Mes</td>
			<td>Residuo</td>
			<td>Modalidad</td>
			<td>Cantidad</td>
		</tr>
		<%for(Residuo r:residuos){%>
			<tr>
			<td><%=r.getMes().toString() %></td>
			<td><%=r.getResiduo() %></td>
			<td><%=r.getModalidad() %></td>
			<td><%=r.getCantidad() %></td>
		</tr>
		<%}%>
	</table>
</body>
</html>