<%@ include file="../WEB-INF/jspf/header.jspf"%>
<title><fmt:message key="content.page.title.info"/></title>
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
            <ctg:message type="${MessageType}" locale="${locale}"/>
            <form action="home.jsp">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary"><fmt:message key="content.page.button.backhome"/></button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>

