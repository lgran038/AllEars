<%-- 
    Document   : response
    Created on : Feb 4, 2018, 5:46:35 AM
    Author     : MSI Laptop
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:useBean id="mybean" scope="session" class="org.allears.code.InputHandler" />
        <jsp:setProperty name="mybean" property="ipaddr"/>
        <h1>Requested IP: <jsp:getProperty name="mybean" property="ipaddr" /></h1>
    </body>
</html>
