<%-- 
    Document   : a1
    Created on : 18 jun. 2021, 15:25:35
    Author     : USER
--%>

<%@page import="Negocio.WebappDANE"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Seleccionar Departamento</title>
    </head>
    <body>
        <div style="text-align: center;"> <br><h3>Escoja el departamento al cual desea ingresar la persona:</h3><br>
    <form action="leerdepartamento.do" method="post">
        <select name="departamento">
            <%
                WebappDANE wa= new WebappDANE();
                out.println(wa.mostrarDepartamentos());

            %>
        </select>
        
        <input type="submit" name="boton" value="Registrar Persona">
    </form>
        </div>
    </body>
</html>
