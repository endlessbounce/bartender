<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
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
</c:choose><nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/controller"><fmt:message key="crumb.bartender"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.profile"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <div class="col-md-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active" id="favourite" data-toggle="pill" href="#favContent" role="tab" aria-controls="favContent" aria-selected="true"><fmt:message key="content.page.profile.favourite"/></a>
            <a class="nav-link" id="myCocktails" data-toggle="pill" href="#myContent" role="tab" aria-controls="myContent" aria-selected="false"><fmt:message key="content.page.profile.mycocktails"/></a>
            <a class="nav-link" id="newCocktail" data-toggle="pill" href="#newContent" role="tab" aria-controls="newContent" aria-selected="false"><fmt:message key="content.page.profile.create"/></a>
        </div>
        <div class="col-md-9 tab-content" id="v-pills-tabContent">
            <div class="tab-pane fade show active" id="favContent" role="tabpanel" aria-labelledby="favourite"><jsp:include page="${ pageContext.request.contextPath }/WEB-INF/jspf/favourite.jspf"/></div>
            <div class="tab-pane fade" id="myContent" role="tabpanel" aria-labelledby="myCocktails"><jsp:include page="${ pageContext.request.contextPath }/WEB-INF/jspf/mycocktails.jspf"/></div>
            <div class="tab-pane fade" id="newContent" role="tabpanel" aria-labelledby="newCocktail"><jsp:include page="${ pageContext.request.contextPath }/WEB-INF/jspf/newcocktail.jspf"/></div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>