<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <%--<title><fmt:message key="content.page.title.catalog"/></title>--%>
    <%--ИМЯ КОКТЕЙЛЯ--%>
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
        <li class="breadcrumb-item active" aria-current="page">ИМЯ коктейля ангулар</li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <div class="col">
            <%--инфа о коктейле--%>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
