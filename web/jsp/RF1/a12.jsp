<%-- 
    Document   : a12
    Created on : 19 jun. 2021, 12:04:53
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrar Persona</title>
    </head>
    <body>
        <div style="text-align: center;"> <br>
        <form name="regpersona" action="validar.do" method="get">
        <label>Cedula: </label> <input type="number" name="cedula" value="0"> <br><br>
        <label>Nombre: </label> <input type="text" name="name" value="ingrese nombre"> <br><br>
        <label>E-mail: </label> <input type="text" name="email" value="ingrese email"> <br><br>
        <label>Direccion: </label> <input type="text" name="direccion" value="ingrese direccion"> <br><br>
        <label>Município: </label> <select name="municipio">
        <%
            String muns=(String)(request.getSession().getAttribute("municipios"));
            out.println(muns);
        %>
        </select> <br>
        <input type="submit" value="Registrar">
        </form>
        </div>
    </body>
</html>
