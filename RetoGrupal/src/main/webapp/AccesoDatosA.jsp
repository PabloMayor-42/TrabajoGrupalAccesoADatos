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
	<%
	Object datos = request.getAttribute("contenido");
	%>
	<table border="1">
		<%
		if (datos instanceof DatosXLS) {
			DatosXLS datosXLS = (DatosXLS) datos;
		%>
		<tr>
			<%
			for (String encab : datosXLS.getEncabezado()) {
			%>
			<td><%=encab%></td>
			<%
			}
			%>
		</tr>
		<%
		for (Object[] celdas : datosXLS.getCuerpo()) {
		%>
		<tr>
			<%
			for (Object celda : celdas) {
			%>
			<td><%=celda%></td>
			<%
			}
			%>
		</tr>
		<%
		}
		}
		
		
		
		%>
	</table>
</body>
</body>
</html>