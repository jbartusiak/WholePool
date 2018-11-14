<%--
  Created by IntelliJ IDEA.
  User: Jakub Bartusiak
  Date: 14.11.2018
  Time: 19:31
  To change this template use File | Settings | File Templates.
--%>
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
        <div class="col-lg-5">
            <div class="page-box">
                <div class="page-content">
                    <h3 class="text-align-center">Zarejestruj się</h3>
                    <hr/>
                    <form>
                        <div class="form-group row">
                            <label for="inputFirstName" class="col-lg-3 col-form-label">Imię</label>
                            <div class="col-lg-9">
                                <input type="text" name="firstname" class="form-control" id="inputFirstName"
                                       placeholder="Imię">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputLastName" class="col-lg-3 col-form-label">Nazwisko</label>
                            <div class="col-lg-9">
                                <input type="text" name="lastname" class="form-control" id="inputLastName"
                                       placeholder="Nazwisko">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputUsername" class="col-lg-3 col-form-label">Nazwa uźytkownika</label>
                            <div class="col-lg-9">
                                <input type="text" name="username" class="form-control" id="inputUsername"
                                       placeholder="Nazwa użytkownika">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputEmail" class="col-lg-3 col-form-label">Adres e-mail</label>
                            <div class="col-lg-9">
                                <input type="email" class="form-control" id="inputEmail" placeholder="Adres e-mail">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputPassword" class="col-lg-3 col-form-label">Hasło</label>
                            <div class="col-lg-9">
                                <input type="password" class="form-control" id="inputPassword" placeholder="Hasło">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="inputDOB" class="col-lg-3 col-form-label">Data urodzenia</label>
                            <div class="col-lg-9">
                                <input type="date" class="form-control" id="inputDOB">
                            </div>
                        </div>
                        <input type="submit" class="btn-lg btn-wholepool btn-block"/>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-lg-1"></div>
        <div class="col-lg-3">
            <div class="page-box">
                <div class="jumbotron page-content-jumbotron" style="padding:50px">
                    <h1 class="text-align-center">Lorem ipsum</h1>
                </div>
                <div class="page-content">

                    <div>
                        <img src="images/sedan.png" style="margin:auto; width:100%"/>
                        <p>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer quis risus eget urna
                            tincidunt eleifend. Phasellus ac turpis sit amet orci malesuada faucibus. Ut nec lorem in
                            justo vulputate rhoncus. Curabitur consequat neque vestibulum lorem elementum porta. Aliquam
                            erat volutpat. Proin ac lorem lectus. Aliquam faucibus fermentum ante, eget vehicula leo
                            congue eget.
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-1"></div>
    </div>
</div>

</body>
</html>
