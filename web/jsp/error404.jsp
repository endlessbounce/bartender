<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.error"/></title>
    <%@ include file="../WEB-INF/jspf/headcontent.jspf" %>
</head>
<body class="error404">
<div class="container">
    <div class="row mt-5">
        <form action="/controller">
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><fmt:message key="content.page.button.backhome"/></button>
            </div>
        </form>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
