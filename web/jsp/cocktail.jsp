<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title>${cocktailName}</title>
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
        <li class="breadcrumb-item"><a href="/controller?command=catalog"><fmt:message key="crumb.catalog"/></a></li>
        <li class="breadcrumb-item active" aria-current="page">${cocktailName}</li>
    </ol>
</nav>
<div class="container mt-3" ng-controller="CocktailCtrl as ctrl">
    <p style="display: none;" id="cocktailId">${cocktailId}</p>
    <p style="display: none;" id="userID">${userID}</p>
    <div class="row">
        <h2 class="font-weight-light">${cocktailName}</h2>
    </div>

    <div class="row">
        <h4 class="font-weight-light mb-3">${cocktailSlogan}</h4>
    </div>

    <div class="row">
        <div class="col-4">
            <div class="container">

                <%--IIMAGE--%>
                <div class="row">
                    <img src="${cocktailUri}" class="img-fluid img-thumbnail" alt="Responsive image">
                </div>

                <%--LIKE/UNLIKE BUTTONS--%>
                <div class="row mt-4">
                    <c:if test="${userName != null}">
                        <button ng-hide="ctrl.isFavourite"
                                type="button"
                                class="btn btn btn-outline-success"
                                ng-click="ctrl.like()">
                            <fmt:message key="content.button.like"/></button>

                        <button ng-show="ctrl.isFavourite"
                                type="button"
                                class="btn btn-outline-danger"
                                ng-click="ctrl.unlike()">
                            <fmt:message key="content.button.unlike"/></button>
                    </c:if>
                </div>
            </div>

        </div>

        <div class="col-8">
            <div class="container">
                <div class="row mb-3">
                    <h6 class="font-weight-light">
                        <fmt:message key="cocktail.type"/> ${cocktailType},
                        <fmt:message key="cocktail.base"/> ${cocktailBase}
                    </h6>
                </div>

                <div class="row">
                    <h6><fmt:message key="cocktail.ingredients"/></h6>
                </div>

                <%--INGREDIENT LIST--%>
                <div class="row mb-3" style="width: 100%;">
                    <ol class="list-group">
                        <c:forEach items="${cocktailIngredients}" var="component" varStatus="loop">
                            <li class="list-group-item font-italic">${component.ingredientName}
                                <c:if test="${component.amount != null}">
                                    : ${component.amount}
                                </c:if>
                            </li>
                        </c:forEach>

                    </ol>
                </div>

                <div class="row">
                    <h6><fmt:message key="cocktail.recipe"/></h6>
                </div>

                <%--RECIPE--%>
                <div class="row">
                    <p class="font-italic">${cocktailRecipe}</p>
                </div>
            </div>

            <br/>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
