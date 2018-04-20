<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.info"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.info"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <c:choose>
            <c:when test="${MessageType != null}">
                <ctg:message type="${MessageType}" locale="${locale}"/>
            </c:when>
            <c:otherwise>
                <log:debug logger="by.khlebnikov.bartender" exception="${pageContext.errorData.throwable}">
                </log:debug>
                <fmt:message key="message.error"/>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="row mt-3">
        <form action="/controller">
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><fmt:message key="content.page.button.backhome"/></button>
            </div>
        </form>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>

