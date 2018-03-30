<%@ include file="../WEB-INF/jspf/header.jspf" %>
<title><fmt:message key="content.page.title.settings"/></title>
<c:choose>
    <c:when test="${userName != null}">
        <%@ include file="../WEB-INF/jspf/loggedinnav.jspf" %>
    </c:when>
    <c:otherwise>
        <%@ include file="../WEB-INF/jspf/navigation.jspf" %>
    </c:otherwise>
</c:choose>
<div class="container">
    <div class="row">
        <div class="col">
            <%--<h1><fmt:message key="message.welcome"/></h1>--%>
        </div>
    </div>
</div>
</body>
</html>
