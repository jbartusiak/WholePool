<%--
  Created by IntelliJ IDEA.
  User: Jakub Bartusiak
  Date: 14.11.2018
  Time: 18:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WholePool - Zaloguj się</title>
    <jsp:include page="jspf/imports.jsp"/>
</head>
<body>
<jsp:include page="jspf/navbar.jsp"/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-2"></div>
            <div class="col-lg-4">
                <div class="page-box">
                    <div class="page-content">
                        <h3 class="text-align-center">Zaloguj się</h3>
                        <hr/>
                        <form>
                            <div class="form-group">
                                <label for="userName">Adres e-mail lub nazwa użytkownika</label>
                                <input type="email" class="form-control" id="userName" aria-describedby="userNameHelp"
                                       placeholder="Wprowadź dane">
                            </div>
                            <div class="form-group">
                                <label for="password">Hasło</label>
                                <input type="password" class="form-control" id="password" placeholder="Password">
                            </div>
                            <button type="submit" class="btn btn-lg btn-wholepool btn-block" style="margin-top:50px;">
                                Zatwierdź
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-lg-1"></div>
            <div class="col-lg-3">
                <div class="page-box">
                    <div class="page-content">
                        <h3 class="text-align-center">Zarejestruj się!</h3>
                        <hr/>
                        <span>Nie posiadasz konta? Załóż je w kilka sekund.</span>
                        <div style="margin-top:50px;padding:0px 100px 0px 100px">
                            <a href="/register" style="text-decoration:none">
                                <button type="button" class="btn btn-lg btn-wholepool btn-block">Załóż konto</button>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-2"></div>
        </div>
    </div>
</body>
</html>
