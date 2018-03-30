<%@ include file="../WEB-INF/jspf/header.jspf" %>
<title><fmt:message key="content.page.title.reminder"/></title>
<%@ include file="../WEB-INF/jspf/navigation.jspf" %>
<div class="container">
    <div class="row">
        <div class="col">
            <h4><fmt:message key="message.reminderemail"/></h4>
            <div class="input-group">
                <form action="controller" method="post">
                    <input class="form-control" type="hidden" name="command" value="remind"/>
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
                        <ctg:message type="${MessageType}"/>
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

