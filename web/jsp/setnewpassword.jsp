<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.reseteviamail"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.newpassword"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.enternewpassword"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="set_new_action"/>

                    <div class="form-group">
                        <label for="password" class="control-label"><fmt:message
                                key="content.page.registration.password"/></label>
                        <div class="form-inline row">
                            <div class="form-group col-sm-6">
                                <input type="password"
                                       class="form-control"
                                <%--minlength="7"--%>
                                <%--maxlength="32"--%>
                                       name="password"
                                       id="password"
                                       placeholder="<fmt:message
                                key="content.placeholder.password"/>"
                                       value="${password}"
                                <%--required--%>
                                >
                                <div class="help-block"><fmt:message key="content.page.registration.min"/></div>
                            </div>
                            <div class="form-group col-sm-6">
                                <input type="password"
                                       class="form-control"
                                       name="confirmation"
                                       placeholder="<fmt:message
                                key="content.placeholder.confirmpassword"/>"
                                       value="${confirmation}"
                                <%--required--%>
                                >
                            </div>
                        </div>
                    </div>
                    <ctg:message type="${MessageType}" locale="${locale}"/>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary"><fmt:message
                                key="content.page.button.submit"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
