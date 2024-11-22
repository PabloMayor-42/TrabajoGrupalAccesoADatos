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
	<h1>TRATAMIENTO DE FICHEROS</h1>
	<form method="post" action="ServletFich">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>Formato del fichero:</td>
							<td><select><option>XLS</option></select></td>
						</tr>
						<tr>
							<td colspan="2"><hr></td>
						</tr>
						<tr>
							<td colspan="2">¿Que desea hacer con el fichero?</td>
						</tr>
						<tr>
							<td>Lectura</td>
							<td><input type="radio" name="accion" value="lectura"></td>
						</tr>
						<tr>
							<td>Escritura</td>
							<td><input type="radio" name="accion" value="escritura"></td>
						</tr>
					</table>
				</td>
				<td>
					<table>
						<% int celdas = ((DatosXLS)application.getAttribute("fichero-xls-contenido")).getEncabezado().length;
							for(int i = 0; i < celdas ; i++){ %>
								<tr><td>Dato <%= i+1 %></td><td><input name="dato-<%=i %>"></td></tr>
							<% }
						%>
					</table>
					<p style="color: red;"><%= (request.getAttribute("faltaParametroFlag") != null && (boolean)request.getAttribute("faltaParametroFlag"))? "(*) Los campos no pueden estar vacios" : "&nbsp;" %></p>
				</td>
			</tr>
		</table>
		<input type="submit" name="opcion" value="Enviar">
	</form>
</body>
</html>