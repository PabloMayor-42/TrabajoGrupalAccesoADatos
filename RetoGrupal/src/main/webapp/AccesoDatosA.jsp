<%@page import="com.retogrupal.xls.entities.DatosXLS"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<body>
<% DatosXLS datosXLS = (DatosXLS) application.getAttribute("fichero"); %>
	<table border="1">
		<tr><%for(String encab : datosXLS.getEncabezado()) { %> <td><%=encab %> </td> <%} %></tr>
		<% for(String[] celdas : datosXLS.getFilas()) { %>
			<tr>
				<% for(String celda : celdas) {%>
					<td><%=celda %></td>
				<%} %>
			</tr>
		<%} %>
	</table>
</body>
</body>
</html>