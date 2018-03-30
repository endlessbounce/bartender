<%@ include file="../WEB-INF/jspf/header.jspf"%>
<title><fmt:message key="content.page.title.info"/></title>
<%@ include file="../WEB-INF/jspf/navigation.jspf"%>
<div class="container">
    <div class="row">
        <div class="col">
            <ctg:message type="${MessageType}"/>
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

