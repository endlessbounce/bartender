<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.home"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.home"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <div class="col">
            <h1 class="text-center"><fmt:message key="message.welcome"/></h1>
            <h5 class="text-uppercase text-center"><fmt:message key="slide.name"/></h5>
            <div id="carouselExampleControls" class="carousel slide mx-auto mt-3" data-ride="carousel" style="width: 800px; height: 400px">
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img class="d-block w-100" src="../img/slide3.jpg" alt="First slide">
                        <div class="carousel-caption d-none d-md-block">
                            <h4 class="font-weight-light text-dark bg-warning"><fmt:message key="slide.item1"/></h4>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img class="d-block w-100" src="../img/slide1.jpg" alt="Second slide">
                        <div class="carousel-caption d-none d-md-block">
                            <h4 class="font-weight-light text-dark bg-warning"><fmt:message key="slide.item2"/></h4>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img class="d-block w-100" src="../img/slide2.jpg" alt="Third slide">
                        <div class="carousel-caption d-none d-md-block">
                            <h4 class="font-weight-light text-dark bg-warning"><fmt:message key="slide.item3"/></h4>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img class="d-block w-100" src="../img/slide4.jpg" alt="Fourth slide">
                        <div class="carousel-caption d-none d-md-block">
                            <h4 class="font-weight-light text-dark bg-warning"><fmt:message key="slide.item4"/></h4>
                        </div>
                    </div>
                </div>
                <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only"><fmt:message key="slide.prev"/></span>
                </a>
                <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only"><fmt:message key="slide.next"/></span>
                </a>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
