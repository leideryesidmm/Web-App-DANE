<%-- 
    Document   : a3
    Created on : 18 jun. 2021, 15:26:10
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consultar Persona</title>
    </head>
    <body>
        <div>
        <form action="validarPersona.do">
        <h3>Por favor digite el nombre de la persona a consultar: <input type="text" name="nombre" value="alguien"></h3>
        <br><br>
        <input type="submit" name="boton" value="Consultar">
        </form>
        </div>
        
    </body>
</html>
