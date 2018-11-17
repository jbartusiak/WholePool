<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>WholePool - Wyszukaj przejazd</title>
    <jsp:include page="jspf/imports.jsp"/>
</head>

<body>

<jsp:include page="jspf/navbar.jsp"/>
<div class="container-fluid" style="margin-top:-110px;">
    <div class="page-box page-content" style="background-color:#DDD; margin-left:50px; margin-right:50px;">
        <form style="padding-left:50px; padding-right:50px;">
            <div class="form-group row">
                <label for="inputEmail4" class="col-sm-2 col-form-label">Miejsce odjazdu:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control " id="inputEmail4" placeholder="wybierz">
                </div>
                <label for="inputEmail4" class="col-sm-2 col-form-label">Miejsce przjazdu:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="inputEmail4" placeholder="wybierz">
                </div>
            </div>
            <div class="form-group row">
                <label for="inputEmail4" class="col-sm-2 col-form-label">Data odjazdu</label>
                <div class="col-sm-4">
                    <input type="date" class="form-control" id="inputEmail4" placeholder="Email">
                </div>
                <label for="inputEmail4" class="col-sm-2 col-form-label">Odjazd po godzinie:</label>
                <div class="col-sm-4">
                    <input type="date" class="form-control" id="inputEmail4" placeholder="Email">
                </div>
            </div>
            <div class="form-group row">
                <button type="button" class=" col-sm-12 btn btn-link text-body"
                        style="padding-left:30px; padding-right:30px;" data-toggle="collapse"
                        data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">Więcej
                    opcji wyszukiwania
                </button>
            </div>
            <div class="collapse" id="collapseExample">
                <div>
                    Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad squid.
                    Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt sapiente ea proident.
                </div>
            </div>
            <div class="form-group row">
                <button type="button" class="btn btn-md btn-wholepool btn-warning"
                        style="padding-left:30px; padding-right:30px; text-align:center;">Szukaj
                </button>
            </div>

        </form>
    </div>
</div>

<div class="container-fluid" style="margin-top:-80px;">
    <div class="page-box" style="margin-left:100px; margin-right:100px;">
        <ul class="list-group">
            <c:forEach items="${rides}" var="ride">
                <li class="list-group-item">
                    <div class="container-fluid result">
                        <a href="/ride/${ride.rideId}">
                            <div class="row">
                                <div class="col-lg-1 text-align-end">
                                    <span class="result-hour">${ride.getRideDetails().getDateOfDeparture()}</span>
                                </div>
                                <div class="col-lg-2">
                                    <h2 class="result-place">${ride.getRouteForThisRide().getRouteFromLocation()}</h2>
                                </div>
                                <div class="col-lg-4"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2 text-align-end">
                                    Cena: <strong> ${ride.getRideDetails().getPrice()} PLN </strong>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-1 text-align-end">
                                    <span class="result-hour">${ride.getRideDetails().getDateOfArrival()}</span>
                                </div>
                                <div class="col-lg-2">
                                    <h2 class="result-place">${ride.getRouteForThisRide().getRouteToLocation()}</h2>
                                </div>
                                <div class="col-lg-4">
                                    Oferta z portalu <img src="images/logo.png" witdh="25px" height="25px"/><strong
                                        style="color:var(--fg-wholepool-accent);">Wholepool</strong>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-2 text-align-end">
                                    <button type="button" class="btn btn-warning btn-wholepool">Zarezerwuj!</button>
                                </div>
                            </div>
                        </a>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>
