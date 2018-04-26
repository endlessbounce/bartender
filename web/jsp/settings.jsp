<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title><fmt:message key="content.page.title.settings"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.settings"/></li>
    </ol>
</nav>
<div class="container mt-3" ng-controller="SettingsCtrl as set" ng-init="set.init('${userID}')">
    <div class="row">
        <%--PILLS--%>
        <div class="col-md-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active"
               id="changeName"
               data-toggle="pill"
               href="#chName"
               role="tab"
               ng-click="set.clean()"
               aria-controls="v-pills-home"
               aria-selected="true"><fmt:message key="settings.change.name"/>
            </a>
            <a class="nav-link"
               id="changeEmail"
               data-toggle="pill"
               href="#chEmail"
               role="tab"
               ng-click="set.clean()"
               aria-controls="v-pills-profile"
               aria-selected="false"><fmt:message key="settings.change.emial"/></a>
            <a class="nav-link"
               id="changePassword"
               data-toggle="pill"
               href="#chPassword"
               role="tab"
               ng-click="set.clean()"
               aria-controls="v-pills-messages"
               aria-selected="false"><fmt:message key="settings.change.password"/></a>
            <a class="nav-link"
               id="delete"
               data-toggle="pill"
               href="#del"
               role="tab"
               ng-click="set.clean()"
               aria-controls="v-pills-settings"
               aria-selected="false"><fmt:message key="settings.delete"/>
            </a>
        </div>

        <%--CONTENT--%>
        <div class="col-md-9 tab-content" id="v-pills-tabContent" ng-show="set.showContent">

            <%--NAME--%>
            <div class="tab-pane fade show active" id="chName" role="tabpanel" aria-labelledby="v-pills-home-tab">
                <form ng-submit="set.updateProfile('name')">
                    <div class="form-group">
                        <label for="name"><fmt:message key="settings.change.name.enter"/></label>
                        <input type="text"
                               ng-model="set.user.name"
                               class="form-control"
                               id="name"
                               maxlength="50"
                               minlength="3"
                               aria-describedby="newName"
                               placeholder="<fmt:message key="settings.change.namenew"/>"
                               required>
                        <small id="operationFailure1" class="form-text text-danger" ng-show="set.operationFailure"
                               style="display: none;">
                            <fmt:message key="settings.update.failure"/>
                        </small>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <fmt:message key="settings.button"/>
                    </button>
                </form>
            </div>

            <%--EMAIL--%>
            <div class="tab-pane fade" id="chEmail" role="tabpanel" aria-labelledby="v-pills-profile-tab">
                <form ng-submit="set.updateProfile('email')">
                    <div class="form-group">
                        <label for="name"><fmt:message key="settings.change.emial.enter"/></label>
                        <input type="text"
                               ng-model="set.user.email"
                               class="form-control"
                               id="email"
                               maxlength="50"
                               minlength="7"
                               aria-describedby="newEmail"
                               placeholder="<fmt:message key="settings.change.emailnew"/>"
                               required>
                        <small id="operationFailure2" class="form-text text-danger" ng-show="set.operationFailure">
                            <fmt:message key="settings.update.failure"/>
                        </small>
                        <small id="operationSuccess1" class="form-text" ng-show="set.operationSuccess">
                            <fmt:message key="settings.update.success"/>
                        </small>
                    </div>

                    <button type="submit" class="btn btn-primary">
                        <fmt:message key="settings.button"/>
                    </button>
                </form>
            </div>

            <%--PASSWORD--%>
            <div class="tab-pane fade" id="chPassword" role="tabpanel" aria-labelledby="v-pills-messages-tab">
                <div class="m-2">
                    <h4 class="font-weight-light mb-3"><fmt:message key="settings.change.password.old"/></h4>
                </div>

                <div class="m-2">

                </div>

                <div class="m-2">
                    <h4 class="font-weight-light mb-3"><fmt:message key="settings.change.password.new"/></h4>
                </div>

                <div class="m-2">

                </div>

                <div class="m-2">
                    <h4 class="font-weight-light mb-3"><fmt:message key="settings.change.password.newrepeat"/></h4>
                </div>

                <div class="m-2">

                </div>
            </div>

            <%--DELETE--%>
            <div class="tab-pane fade" id="del" role="tabpanel" aria-labelledby="v-pills-settings-tab">
                <!-- Button trigger modal -->
                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#exampleModal">
                    <fmt:message key="settings.delete.button"/>
                </button>
                <small id="operationFailure" class="form-text" ng-show="set.operationFailure">
                    <fmt:message key="settings.update.failure"/>
                </small>

                <!-- Modal -->
                <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
                     aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLabel"><fmt:message
                                        key="settings.delete.sure.title"/></h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <fmt:message key="settings.delete.sure"/>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                    <fmt:message key="settings.delete.button.close"/>
                                </button>
                                <button type="button" class="btn btn-danger" data-dismiss="modal"
                                        ng-click="set.deleteProfile()">
                                    <fmt:message key="settings.delete.button"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>
