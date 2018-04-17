<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.registration"/></title>
    <%@ include file="../WEB-INF/jspf/headcontent.jspf" %>
</head>
<body>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/controller"><fmt:message key="crumb.bartender"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.registration"/></li>
    </ol>
</nav>
<div class="container mt-3">
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
                               value="${e:forHtmlAttribute(name)}"
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
                               value="${e:forHtmlAttribute(email)}"
                        <%--required--%>
                        >
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="password" class="control-label"><fmt:message
                                    key="content.page.registration.password"/></label>
                            <input type="password"
                                   class="form-control"
                            <%--minlength="7"--%>
                            <%--maxlength="32"--%>
                                   name="password"
                                   id="password"
                                   placeholder="<fmt:message
                                key="content.placeholder.password"/>"
                                   value="${e:forHtmlAttribute(password)}"
                            <%--required--%>
                            >
                            <small id="passwordHelpBlock" class="form-text text-muted">
                                <fmt:message key="content.page.registration.min"/>
                            </small>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="confirmation" class="control-label"><fmt:message
                                    key="content.page.registration.confirmation"/></label>
                            <input type="password"
                                   class="form-control"
                                   name="confirmation"
                                   id="confirmation"
                                   placeholder="<fmt:message
                                key="content.placeholder.confirmpassword"/>"
                                   value="${e:forHtmlAttribute(confirmation)}"
                            <%--required--%>
                            >
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
