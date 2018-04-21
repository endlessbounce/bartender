<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html>
<head>
    <title><fmt:message key="content.page.title.glossary"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.glossary"/></li>
    </ol>
</nav>
<div class="container mt-3">
    <div class="row">
        <%--PILLS--%>
        <div class="col-md-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active"
               id="bartending101"
               data-toggle="pill"
               href="#101home"
               role="tab"
               aria-controls="v-pills-home"
               aria-selected="true"><fmt:message key="glossary.101"/>
            </a>
            <a class="nav-link"
               id="ingredients"
               data-toggle="pill"
               href="#ingred"
               role="tab"
               aria-controls="v-pills-profile"
               aria-selected="false"><fmt:message key="glossary.ingredients"/></a>
            <a class="nav-link"
               id="drinkTypes"
               data-toggle="pill"
               href="#dType"
               role="tab"
               aria-controls="v-pills-messages"
               aria-selected="false"><fmt:message key="glossary.drinktypes"/></a>
            <a class="nav-link"
               id="glassware"
               data-toggle="pill"
               href="#glass"
               role="tab"
               aria-controls="v-pills-settings"
               aria-selected="false"><fmt:message key="glossary.glassware"/>
            </a>
            <a class="nav-link"
               id="mixology"
               data-toggle="pill"
               href="#mix"
               role="tab"
               aria-controls="v-pills-settings"
               aria-selected="false"><fmt:message key="glossary.mixology"/>
            </a>
        </div>

        <%--CONTENT--%>
        <div class="col-md-9 tab-content" id="v-pills-tabContent">

            <%--101--%>
            <div class="tab-pane fade show active" id="101home" role="tabpanel" aria-labelledby="v-pills-home-tab">

                <div class="m-2">
                    <p><fmt:message key="glossary.101.P1"/></p>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L1"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="8">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L1.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L2"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="8">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L2.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L3"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="7">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L3.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L4"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="21">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L4.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L5"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="8">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L5.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L6"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="7">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L6.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="m-2">
                    <h4 class="font-weight-light"><fmt:message key="glossary.101.L7"/></h4>
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="21">
                            <li class="list-group-item">
                                <fmt:message key="glossary.101.L7.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <%--INGREDIENTS--%>
            <div class="tab-pane fade" id="ingred" role="tabpanel" aria-labelledby="v-pills-profile-tab">
                <div class="m-2">
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="87">
                            <li class="list-group-item">
                                <fmt:message key="glossary.ingredients.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <%--DINRK TYPES--%>
            <div class="tab-pane fade" id="dType" role="tabpanel" aria-labelledby="v-pills-messages-tab">
                <div class="m-2">
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="18">
                            <li class="list-group-item">
                                <fmt:message key="glossary.drinktypes.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <%--GLASSWARE--%>
            <div class="tab-pane fade" id="glass" role="tabpanel" aria-labelledby="v-pills-settings-tab">
                <div class="m-2">
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="21">
                            <li class="list-group-item">
                                <fmt:message key="glossary.glassware.${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <%--MIXOLOGY--%>
            <div class="tab-pane fade" id="mix" role="tabpanel" aria-labelledby="v-pills-settings-tab">
                <div class="m-2">
                    <ul class="list-group">
                        <c:forEach var="i" begin="1" end="6">
                            <li class="list-group-item">
                                <fmt:message key="glossary.mixology.L${i}"/>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
