<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title><fmt:message key="content.page.title.searchresults"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.searchresult"/></li>
    </ol>
</nav>

<div class="container mt-3" ng-controller="CatalogCtrl as cat">
    <div class="row">
        <div class="col-md-3">

            <%--drink type droplist--%>
            <div class="form-group">
                <label for="drinkType"><fmt:message key="drink.type"/></label>
                <select class="form-control" id="drinkType">
                    <option selected ng-click="cat.addDrinkType($event)" value="delete"><fmt:message
                            key="drink.default"/></option>
                    <option ng-repeat="type in cat.drinkTypes"
                            value="{{type}}"
                            ng-click="cat.addDrinkType($event)">{{type}}
                    </option>
                </select>
            </div>

            <%--base drink droplist--%>
            <div class="form-group">
                <label for="baseDrink"><fmt:message key="drink.base"/></label>
                <select class="form-control" id="baseDrink">
                    <option selected ng-click="cat.addBaseDrink($event)" value="delete"><fmt:message
                            key="drink.default"/></option>
                    <option ng-repeat="drink in cat.baseDrinks"
                            value="{{drink}}"
                            ng-click="cat.addBaseDrink($event)">{{drink}}
                    </option>
                </select>
            </div>

            <%--ingredients check box form--%>
            <label for="ingDiv" style="font-weight: normal"><fmt:message key="drink.ingredient"/></label><br>
            <div class="form-check ingredient" id="ingDiv">
                <div ng-repeat="ingredient in cat.ingredients">
                    <label class="form-check-label ml-3 mt-1 mb-1">
                        <input class="form-check-input"
                               type="checkbox"
                               id="{{ingredient}}"
                               ng-click="cat.addIngredient($event)">{{ingredient}}
                    </label>
                    <br>
                </div>
            </div>

            <%--reset button--%>
            <div class="form-group mt-3">
                <button type="button" class="btn btn-primary btn-sm" ng-click="cat.reset()"><fmt:message
                        key="content.button.reset"/></button>
            </div>

            <%--show pages button group--%>
            <label for="btngroup" style="font-weight: normal"><fmt:message
                    key="content.pagination.show"/></label><br>
            <div class="btn-toolbar" id="btngroup" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group mr-2" role="group" aria-label="First group">
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(12)">12
                    </button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(24)">24
                    </button>
                    <button type="button" class="btn btn-secondary" ng-click="cat.changeDisplayNumber(48)">48
                    </button>
                </div>
            </div>
        </div>

        <div class="col-md-9" id="onLoad" style="visibility: hidden;">
            <div class="row">
                <h2 class="font-weight-light"><fmt:message key="message.searchresult"/> '${e:forHtmlContent(text)}'</h2>
            </div>

            <div class="row">

                <%--cocktail card--%>
                <div class="card float-left m-2" style="width: 16rem;"
                     dir-paginate="card in cat.cocktails | filter: {name: '${e:forHtmlContent(text)}'} | itemsPerPage: cat.showPages track by $index">
                    <img class="card-img-top" ng-src="${ pageContext.request.contextPath }{{card.uri}}"
                         alt="Card image cap">
                    <div class="card-body">
                        <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                        <p class="card-text" style="height: 50px; overflow: auto;">
                            <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}<span
                                    ng-if="!$last">, </span></span>
                        </p>
                        <a ng-href="/controller?command=cocktail&id={{card.id}}"
                           class="btn btn-primary"><fmt:message
                                key="content.pagination.view"/></a>
                    </div>
                </div>
            </div>
            <div class="row m-3">
                <%--paginaton. change default template classes to bootstrap 4--%>
                <dir-pagination-controls boundary-links="true"
                                         template-url="${ pageContext.request.contextPath }/js/dirPagination.tpl.html">
                </dir-pagination-controls>
            </div>
        </div>
    </div>
</div>

<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
