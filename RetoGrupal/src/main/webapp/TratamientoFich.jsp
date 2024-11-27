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
	<form method="post" action="ServletFichero">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>Formato del fichero:</td>
							<td>
								<select name="fichero">
									<option>XLS</option>
									<option>YAML</option>
									<option>JSON</option>
									<option>CSV</option>
									<option>XML</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="2"><hr></td>
						</tr>
						<tr>
							<td colspan="2">¿Que desea hacer con el fichero?</td>
						</tr>
						<tr>
							<td>Lectura:</td>
							<td><input type="radio" name="accion" value="lectura" checked></td>
						</tr>
						<tr>
							<td>Escritura:</td>
							<td><input type="radio" name="accion" value="escritura"></td>
						</tr>
					</table>
				</td>
				<td>
					<table>
						<tr><td>DATO1: <input type="text" name="dato1"></td></tr>
						<tr><td>DATO2: <input type="text" name="dato2"></td></tr>
						<tr><td>DATO3: <input type="text" name="dato3"></td></tr>
						<tr><td>DATO4: <input type="text" name="dato4"></td></tr>
						<tr><td>DATO5: <input type="text" name="dato5"></td></tr>
						<tr><td>DATO6: <input type="text" name="dato6"></td></tr>
					</table>
					<p style="color: red;"><%= (request.getAttribute("faltaParametroFlag") != null && (boolean)request.getAttribute("faltaParametroFlag"))? "(*) Los campos no pueden estar vacios" : "&nbsp;" %></p>
				</td>
			</tr>
		</table>
		<input type="submit" name="bt" value="Enviar">
	</form>
</body>
</html>