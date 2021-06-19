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
        <title>JSP Page</title>
    </head>
    <body>
    <from action="sa1.do">
        <select name="departamento">
            <%
                WebappDANE wa= new WebappDANE(); 
                out.println(wa.mostrarDepartamentos()); 

            %>
        </select>
    </from>
    </body>
</html>
