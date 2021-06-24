<%-- 
    Document   : a31
    Created on : 19 jun. 2021, 12:05:49
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Datos Persona</title>
    </head>
    <body>
       <div style="text-align: center;"> <br><br>
        <%
            out.println(request.getSession().getAttribute("tabla"));
        %>
        <br><br>
        <a href="../../index.html"><button>Volver al inicio</button></a>
        </div>
    </body>
</html>
