<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Author: Johns Castillo Valencia, john.gnu@gmail.com -->
<!DOCTYPE html>
<html lang="es">
    <head>        
        <title>SWI - Iniciar sesión</title>
        <script type="text/javascript">
            if (top != self) {
                top.location = '<c:url value="/"/>main';
            }
        </script> 
        <%@include file="../ExtJSScripts-ES.jsp"%>  

        <script type="text/javascript" src="js/login/loginform.js"></script>
        <style type="text/css">
            body {
                background: url(images/asfi05.jpg);
                padding: 10px;
                font-family: tahoma;
                font-size:11px;
            }
            #header {
                color: #FFFFFF;
            }
            #header h1 {
                font-family: tahoma;
                font-size: 16px;                                
            }
        </style>
        <script type="text/javascript">

            Ext.ns('domain');

            domain.Manager = {
                init: function() {
                    var ln = domain.security.Login();
                    ln.show();
                }
            }

            Ext.onReady(domain.Manager.init, domain.Manager);
        </script>
    <body>
        <div id="header">
            <div style="">
                <h1>SWI</h1>            
                <p>Sistema de Consulta de Informaci&oacute;n Institucional<p> 
            </div>
        </div>    
    </body>
</html>
