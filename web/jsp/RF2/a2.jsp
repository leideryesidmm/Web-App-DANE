<%-- 
    Document   : a2
    Created on : 18 jun. 2021, 15:26:00
    Author     : USER
--%>

<%@page import="Negocio.WebappDANE"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div style="text-align: center;"> <br><h3>Escoja el departamento del cual desea consultar información:</h3><br>
    <form action="leerdepartamento.do" method="post">
        <select name="departamento">
            <%
                WebappDANE wa= new WebappDANE();
                out.println(wa.mostrarDepartamentos());
            %>
        </select>
        
        <input type="submit" name="boton" value="Consultar">
    </form>
        </div>
    </body>
</html>
