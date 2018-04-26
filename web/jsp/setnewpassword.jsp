<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
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
        <h4><fmt:message key="message.enternewpassword"/></h4>
    </div>

    <div class="row">
        <form action="controller" method="post" style="width: 100%;">
            <input class="form-control" type="hidden" name="command" value="set_new_action"/>

            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="password"><fmt:message key="content.page.registration.password"/></label>
                    <input type="password"
                           class="form-control"
                           minlength="7"
                           maxlength="32"
                           name="password"
                           id="password"
                           placeholder="<fmt:message
                                key="content.placeholder.password"/>"
                           value="${password}"
                           required>
                    <small id="passwordHelpBlock" class="form-text text-muted">
                        <fmt:message key="content.page.registration.min"/>
                    </small>
                </div>

                <div class="form-group col-md-6">
                    <label for="confirm"><fmt:message key="content.page.registration.confirmation"/></label>
                    <input type="password"
                           id="confirm"
                           class="form-control"
                           name="confirmation"
                           placeholder="<fmt:message
                                key="content.placeholder.confirmpassword"/>"
                           value="${confirmation}"
                           required>
                </div>
            </div>

            <div class="form-row">
                <ctg:message type="${MessageType}" locale="${locale}"/>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary"><fmt:message
                            key="content.page.button.submit"/></button>
                </div>
            </div>
        </form>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>

