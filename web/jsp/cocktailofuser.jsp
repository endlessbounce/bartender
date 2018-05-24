<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title>${e:forHtmlContent(cocktail.name)}</title>
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
        <li class="breadcrumb-item"><a href="/controller?command=profile"><fmt:message key="crumb.profile"/></a></li>
        <li class="breadcrumb-item"><a href="/controller?command=profile&section=created"><fmt:message
                key="crumb.usercocktail"/></a></li>
        <li class="breadcrumb-item active" aria-current="page">${e:forHtmlContent(cocktail.name)}</li>
    </ol>
</nav>

<div class="container mt-3" ng-controller="UserCocktailCtrl as ctrl" ng-init="ctrl.init('${userID}')">
    <div class="row">
        <h2 class="font-weight-light">${e:forHtmlContent(cocktail.name)}</h2>
    </div>

    <div class="row">
        <h4 class="font-weight-light mb-3">${e:forHtmlContent(cocktail.slogan)}</h4>
    </div>

    <div class="row">
        <div class="col-4">
            <div class="container">

                <%--IIMAGE--%>
                <div class="row">
                    <img src="${ pageContext.request.contextPath }${e:forHtmlContent(cocktail.uri)}" class="img-fluid img-thumbnail"
                         alt="Responsive image">
                </div>

                <%--EDIT/DELETE BUTTONS--%>
                <div class="row mt-4" id="likeButtons" ng-show="ctrl.showButtons">
                    <c:if test="${userName != null}">

                        <button class="btn btn-outline-info" ng-click="ctrl.editCreatedC('${cocktail.id}')">
                            <img src="${ pageContext.request.contextPath }/img/edit.png" width="20"/>
                        </button>

                        <button class="btn btn-outline-info ml-3" ng-click="ctrl.deleteCreatedC('${cocktail.id}')">
                            <img src="${ pageContext.request.contextPath }/img/delete.png" width="20"/>
                        </button>

                    </c:if>
                </div>
            </div>

        </div>

        <div class="col-8">
            <div class="container">
                <div class="row mb-3">
                    <h6 class="font-weight-light">
                        <fmt:message key="cocktail.type"/> ${e:forHtmlContent(cocktail.type)},
                        <fmt:message key="cocktail.base"/> ${e:forHtmlContent(cocktail.baseDrink)}
                    </h6>
                </div>

                <div class="row">
                    <h6><fmt:message key="cocktail.ingredients"/></h6>
                </div>

                <%--INGREDIENT LIST--%>
                <div class="row mb-3" style="width: 100%;">
                    <ol class="list-group">
                        <c:forEach items="${cocktail.ingredientList}" var="component" varStatus="loop">
                            <li class="list-group-item font-italic">${e:forHtmlContent(component.ingredientName)}
                                <c:if test="${component.amount != null}">
                                    : ${e:forHtmlContent(component.amount)}
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
                    <p class="font-italic">${e:forHtmlContent(cocktail.recipe)}</p>
                </div>
            </div>

            <br/>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
