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

            <%--drink type droplist--%>
            <div class="form-group">
                <label for="drinkType"><fmt:message key="drink.type"/></label>
                <select class="form-control" id="drinkType">
                    <option selected><fmt:message key="drink.default"/></option>
                    <option ng-repeat="type in cat.drinkTypes" value="{{type}}">{{type}}</option>
                </select>
            </div>

            <%--base drink droplist--%>
            <div class="form-group">
                <label for="baseDrink"><fmt:message key="drink.base"/></label>
                <select class="form-control" id="baseDrink">
                    <option selected><fmt:message key="drink.default"/></option>
                    <option ng-repeat="drink in cat.baseDrinks" value="{{drink}}">{{drink}}</option>
                </select>
            </div>

            <%--ingredients check box form--%>
            <label for="ingDiv" style="font-weight: normal"><fmt:message key="drink.ingredient"/></label><br>
            <div class="form-check ingredient" id="ingDiv">
                <div ng-repeat="ingredient in cat.ingredients">
                    <label class="form-check-label ml-3 mt-1 mb-1">
                        <input class="form-check-input" type="checkbox" id="scb1" name="scb1" value="{{ingredient}}">{{ingredient}}
                    </label>
                    <br>
                </div>
            </div>

            <%--show pages button group--%>
            <label class="mt-3" for="btngroup" style="font-weight: normal"><fmt:message
                    key="content.pagination.show"/></label><br>
            <div class="btn-toolbar" id="btngroup" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group mr-2" role="group" aria-label="First group">
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(12)">12</button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(24)">24</button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(48)">48</button>
                </div>
            </div>
        </div>

        <div class="col-md-9">
            <div class="row">

                <%--cocktail card--%>
                <div class="card float-left m-2" style="width: 16rem;"
                     dir-paginate="card in cat.cocktails | itemsPerPage: cat.showPages track by $index">
                    <img class="card-img-top" src="{{card.uri}}" alt="Card image cap">
                    <div class="card-body">
                        <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                        <p class="card-text" style="height: 50px; overflow: auto;">
                            <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}<span ng-if="!$last">, </span></span>
                        </p>
                        <a href="/controller?command=cocktail&id={{card.id}}" class="btn btn-primary"><fmt:message key="content.pagination.view"/></a>
                    </div>
                </div>
            </div>
            <div class="row m-3">
                <div>

                    <%--paginaton. change default template classes to bootstrap 4--%>
                    <dir-pagination-controls boundary-links="true"
                                             on-page-change="getNextBatchOfCocktails(newPageNumber, oldPageNumber)"
                                             template-url="../js/dirPagination.tpl.html"></dir-pagination-controls>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
