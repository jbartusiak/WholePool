<%--
  Created by IntelliJ IDEA.
  User: Jakub Bartusiak
  Date: 12.11.2018
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WholePool - Strona główna</title>
    <jsp:include page="jspf/imports.jsp"/>
</head>
<body>
<jsp:include page="jspf/navbar.jsp"/>
<div class="container">
    <div class="page-box">
        <div class="jumbotron page-content-jumbotron text-align-start">
            <h1>Wholepool</h1>
            <span class="color-white">...twoje przejazdy w jednym miejscu.</span>
        </div>
        <div class="page-content">
            <h3 class="text-align-center">Fajnie, że wpadłeś!</h3>
            <span>Serwis Wholepool pomoźe Ci szybko odszukać przejazdy, które mogą Cię zainteresować. Aby rozpocząć, skorzystaj z przycisków na górze ekranu, zaloguj się lub załóż konto teraz!</span>
            <div style="margin-top:50px;padding:0px 100px 0px 100px">
                <a href="/register" style="text-decoration:none">
                    <button type="button" class="btn btn-lg btn-wholepool btn-block">Załóż konto</button>
                </a>
            </div>
            <div style="margin-top:10px; padding:0px 100px 0px 100px">
                <a href="/login" style="text-decoration:none">
                    <button type="button" class="btn btn-lg btn-wholepool btn-block">Zaloguj się</button>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
