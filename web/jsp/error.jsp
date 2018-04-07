<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.error"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.error"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row center-block">
        <h3><fmt:message key="content.error.message"/></h3><br/>
        <form action="/controller">
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><fmt:message key="content.page.button.backhome"/></button>
            </div>
        </form>
    </div>
    <div class="row center-block">
        <div class="col">
            <fmt:message key="content.error.fail"/>${pageContext.errorData.requestURI}
        </div>
    </div>
    <div class="row center-block">
        <div class="col">
            <fmt:message key="content.error.servlet"/>${pageContext.errorData.servletName}
        </div>
    </div>
    <div class="row center-block">
        <div class="col">
            <fmt:message key="content.error.status"/>${pageContext.errorData.statusCode}
        </div>
    </div>
    <div class="row center-block">
        <div class="col">
            <fmt:message key="content.error.exception"/>${pageContext.errorData.throwable}
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
