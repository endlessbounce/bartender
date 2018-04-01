<%@ include file="../WEB-INF/jspf/header.jspf" %>
<title><fmt:message key="content.page.title.registration"/></title>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<div class="container">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.fillin"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="confirm"/>
                    <div class="form-group">
                        <label for="inputName" class="control-label"><fmt:message
                                key="content.page.registration.name"/></label>
                        <input type="text"
                               class="form-control"
                               id="inputName"
                               name="name"
                               placeholder="<fmt:message
                                key="content.placeholder.name"/>"
                               value="${name}"
                               autofocus
                        <%--required--%>
                        >
                    </div>

                    <div class="form-group">
                        <label for="inputEmail" class="control-label"><fmt:message
                                key="content.page.registration.email"/></label>
                        <input type="email"
                               class="form-control"
                               id="inputEmail"
                               name="email"
                               placeholder="<fmt:message
                                key="content.placeholder.email"/>"
                               value="${email}"
                        <%--required--%>
                        ></br>
                    </div>

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
</body>
</html>
