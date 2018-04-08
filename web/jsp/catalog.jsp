<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title><fmt:message key="content.page.title.catalog"/></title>
    <%@ include file="../WEB-INF/jspf/headcontent.jspf" %>
</head>
<body>
<c:choose>
    <c:when test="${userName != null}">
        <%@ include file="../WEB-INF/jspf/navigationlogged.jspf" %>
    </c:when>
    <c:otherwise>
        <%@ include file="../WEB-INF/jspf/navigation.jspf" %>
    </c:otherwise>
</c:choose>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/controller"><fmt:message key="crumb.bartender"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.catalog"/></li>
    </ol>
</nav>

<div class="container mt-3" ng-controller="CatalogCtrl as cat">
    <div class="row">
        <div class="col-md-3">
            <div class="form-group">
                <label for="drinkType"><fmt:message key="drink.type"/></label>
                <select class="form-control" id="drinkType">
                    <option selected><fmt:message key="drink.default"/></option>
                    <option value="___">2</option>
                    <option value="___">3</option>
                    <option value="___">4</option>
                    <option value="___">5</option>
                </select>
            </div>
            <div class="form-group">
                <label for="baseDrink"><fmt:message key="drink.base"/></label>
                <select class="form-control" id="baseDrink">
                    <option selected><fmt:message key="drink.default"/></option>
                    <option value="___">2</option>
                    <option value="___">3</option>
                    <option value="___">4</option>
                    <option value="___">5</option>
                </select>
            </div>
            <label for="ingDiv" style="font-weight: normal"><fmt:message key="drink.ingredient"/></label><br>
            <div class="form-check ingredient" id="ingDiv">
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox" id="scb1"
                                                                      name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb12" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb13" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb14" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb15" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb16" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb17" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3"><input class="form-check-input" type="checkbox" id="scb18"
                                                            name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb19" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb111" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb1122" name="scb1" value="1">Name</label>
                <br>
                <label class="form-check-label ml-3 mt-1 mb-1"><input class="form-check-input" type="checkbox"
                                                                      id="scb1112" name="scb1" value="1">Name</label>
                <br>
            </div>
            <label class="mt-3" for="btngroup" style="font-weight: normal"><fmt:message key="content.pagination.show"/></label><br>
            <div class="btn-toolbar" id="btngroup" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group mr-2" role="group" aria-label="First group">
                    <button type="button" class="btn btn-secondary" ng-click="cat.displayNumber(12)">12</button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.displayNumber(24)">24</button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.displayNumber(48)">48</button>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="row">
                <div class="card float-left m-2" style="width: 16rem;" dir-paginate="card in cat.todos | itemsPerPage: cat.showPages track by $index">
                    <img class="card-img-top" src="../img/cocktail.jpg" alt="Card image cap">
                    <div class="card-body">
                        <h5 class="card-title" ng-bind="card">{{card}}</h5>
                        <p class="card-text">Some quick example text to build on the card title and make up the bulk of the
                            card's content.</p>
                        <a href="#" class="btn btn-primary"><fmt:message key="content.page.title.catalog"/></a>
                    </div>
                </div>
            </div>

            <div class="row m-3">
                <div>
                    <%--change default template classes to bootstrap 4--%>
                    <dir-pagination-controls boundary-links="true" template-url="../js/dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
