<%@ include file="../WEB-INF/jspf/header.jspf" %>
<title><fmt:message key="content.page.title.singin"/></title>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<div class="container">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.login"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="login_action"/>
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
                        >
                    </div>

                    <div class="form-group">
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

                    <div class="form-group">
                        <small class="form-text text-muted">
                            <fmt:message key="message.notsignedup"/><a href="/controller?command=register"><fmt:message
                                key="message.signup"/></a>
                        </small>
                        <br/>
                        <small class="form-text text-muted">
                            <fmt:message key="message.forgot"/><a href="/controller?command=reset"><fmt:message
                                key="message.reset"/></a>
                        </small>
                    </div>

                    <div class="form-check">
                        <input type="checkbox"
                               class="form-check-input"
                               id="logged"
                               size="30"
                               name="logged"
                               value="true">
                        <label for="logged" class="form-check-label"><fmt:message
                                key="content.page.registration.logged"/></label>
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
</body>
</html>