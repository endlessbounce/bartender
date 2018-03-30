<%@ include file="../WEB-INF/jspf/header.jspf" %>
<title><fmt:message key="content.page.title.singin"/></title>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<div class="container">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.login"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="login"/>
                    <div class="form-group">
                        <label for="inputEmail" class="control-label"><fmt:message
                                key="content.page.registration.email"/></label>
                        <input type="email"
                               class="form-control"
                               id="inputEmail"
                               size="30"
                               name="email"
                               placeholder="<fmt:message
                                key="content.placeholder.email"/>"
                               value="${email}"
                               <%--required--%>
                        ></br>
                        <label for="password" class="control-label"><fmt:message
                                key="content.page.registration.password"/></label>
                        <input type="password"
                               class="form-control"
                               name="password"
                               id="password"
                               size="30"
                               placeholder="<fmt:message
                                key="content.placeholder.password"/>"
                               value="${password}"
                               <%--required--%>
                        >
                    </div>
                    <p>
                        <small>
                            <fmt:message key="message.notsignedup"/><a href="registration.jsp"><fmt:message
                                key="message.signup"/></a></br>
                            <fmt:message key="message.forgor"/><a href="reminder.jsp"><fmt:message
                                key="message.remindme"/></a>
                        </small>
                    </p>
                    <ctg:message type="${MessageType}"/>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary"><fmt:message
                                key="content.page.button.submit"/></button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>

