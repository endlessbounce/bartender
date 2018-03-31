<%@ include file="../WEB-INF/jspf/header.jspf"%>
<title><fmt:message key="content.page.title.error"/></title>
<c:choose>
    <c:when test="${userName != null}">
        <%@ include file="../WEB-INF/jspf/loggedinnav.jspf" %>
    </c:when>
    <c:otherwise>
        <%@ include file="../WEB-INF/jspf/navigation.jspf" %>
    </c:otherwise>
</c:choose>
<div class="container">

    <div class="row center-block">
        <h3><fmt:message key="content.error.message"/></h3>
        <form action="/jsp/home.jsp">
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
</body>
</html>