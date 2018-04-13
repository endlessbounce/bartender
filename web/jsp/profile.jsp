<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title><fmt:message key="content.page.title.profile"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.profile"/></li>
    </ol>
</nav>
<div class="container mt-3" ng-controller="ProfileCtrl as prof">
    <p style="display: none;" id="userID">${userID}</p>
    <div class="row">
        <%--PILLS--%>
        <div class="col-md-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active" id="favourite" data-toggle="pill" href="#favContent" role="tab"
               aria-controls="favContent" aria-selected="true"><fmt:message key="content.page.profile.favourite"/></a>
            <a class="nav-link" id="myCocktails" data-toggle="pill" href="#myContent" role="tab"
               aria-controls="myContent" aria-selected="false"><fmt:message key="content.page.profile.mycocktails"/></a>
            <a class="nav-link" id="newCocktail" data-toggle="pill" href="#newContent" role="tab"
               aria-controls="newContent" aria-selected="false"><fmt:message key="content.page.profile.create"/></a>
        </div>

        <%--CONTENT--%>
        <div class="col-md-9 tab-content" id="v-pills-tabContent">

            <%--FAVOURITE COCKTAILS--%>
            <div class="tab-pane fade show active" id="favContent" role="tabpanel" aria-labelledby="favourite">
                <div id="firstItem" style="visibility: hidden;">

                    <div class="row" ng-if="prof.cocktails.length == 0">
                        <h4 class="font-weight-light mb-3"><fmt:message key="content.page.profile.favourite.noneyet"/></h4>
                    </div>

                    <div class="row">
                        <%--CARDS--%>
                        <div class="card float-left m-2" style="width: 16rem;"
                             dir-paginate="card in prof.cocktails | itemsPerPage: prof.showPages track by $index">
                            <img class="card-img-top" src="{{card.uri}}" alt="Card image cap">
                            <%--BODY--%>
                            <div class="card-body">
                                <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                                <p class="card-text" style="height: 50px; overflow: auto;">
                            <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}<span
                                    ng-if="!$last">, </span></span>
                                </p>
                                <a href="/controller?command=cocktail&id={{card.id}}" class="btn btn-primary"><fmt:message
                                        key="content.pagination.view"/></a>
                                <a href="#" ng-click="prof.unlike(card.id)" class="btn btn-danger ml-3"><fmt:message
                                        key="content.button.unlike"/></a>
                            </div>
                        </div>
                    </div>

                    <div class="row m-3" ng-hide="prof.cocktails.length <= 12">
                        <div class="btn-toolbar" id="btngroup" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(12)">12</button>
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(24)">24</button>
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(48)">48</button>
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
            <div class="tab-pane fade" id="myContent" role="tabpanel" aria-labelledby="myCocktails">
                <jsp:include page="${ pageContext.request.contextPath }/WEB-INF/jspf/mycocktails.jspf"/>
            </div>
            <div class="tab-pane fade" id="newContent" role="tabpanel" aria-labelledby="newCocktail">
                <jsp:include page="${ pageContext.request.contextPath }/WEB-INF/jspf/newcocktail.jspf"/>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>