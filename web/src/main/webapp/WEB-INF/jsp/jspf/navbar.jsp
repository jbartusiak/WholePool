<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Jakub Bartusiak
  Date: 12.11.2018
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="navbar-brand">
        <a id="wholepool-title" class="navbar-brand" href="#">
            <img class="d-inline-block align-top" src="images/logo.png" height="50" width="50"/>&nbspWholepool
        </a>

    </div>

    <div class="navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/search">Wyszukaj przejazd</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/offer">Dodaj przejazd</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/myRides">Moje przejazdy</a>
            </li>
        </ul>
    </div>

    <div>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    Cześć, @User &nbsp <img class="d-inline-block" src="images/logo.png" height="50" width="50"/>
                </a>

                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="/login">Zaloguj się</a>
                    <a class="dropdown-item" href="/register">Zarejestruj się</a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/help">Pomoc</a>
                </div>

            </li>
        </ul>
    </div>
</nav>