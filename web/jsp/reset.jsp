<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.reset"/></title>
    <%@ include file="../WEB-INF/jspf/headcontent.jspf" %>
</head>
<body>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="/controller"><fmt:message key="crumb.bartender"/></a></li>
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.reset"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.resetpassword"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="reset_action"/>
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
                        <div class="help-block"><br/></div>
                    </div>
                    <div class="form-group">
                        <ctg:message type="${MessageType}" locale="${locale}"/>
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

