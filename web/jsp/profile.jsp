<%@ include file="../WEB-INF/jspf/imports.jspf" %>
<html ng-app="catalog">
<head>
    <title><fmt:message key="content.page.title.profile"/></title>
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
        <li class="breadcrumb-item active" aria-current="page"><fmt:message key="crumb.profile"/></li>
    </ol>
</nav>
<div class="container mt-3" ng-controller="ProfileCtrl as prof">
    <p style="display: none;" id="userID">${userID}</p>
    <div class="row">
        <%--PILLS--%>
        <div class="col-md-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
            <a class="nav-link active" id="favourite" data-toggle="pill" href="#favContent" role="tab"
               aria-controls="favContent" aria-selected="true"><fmt:message key="content.page.profile.favourite"/></a>
            <a class="nav-link" id="myCocktails" data-toggle="pill" href="#myContent" role="tab"
               aria-controls="myContent" aria-selected="false"><fmt:message key="content.page.profile.mycocktails"/></a>
            <a class="nav-link" id="newCocktail" data-toggle="pill" href="#newContent" role="tab"
               aria-controls="newContent" aria-selected="false"><fmt:message key="content.page.profile.create"/></a>
        </div>

        <%--CONTENT--%>
        <div class="col-md-9 tab-content" id="v-pills-tabContent">

            <%--FAVOURITE COCKTAILS--%>
            <div class="tab-pane fade show active" id="favContent" role="tabpanel" aria-labelledby="favourite">
                <div id="firstItem" style="visibility: hidden;">

                    <div class="row" ng-if="prof.cocktails.length == 0">
                        <h4 class="font-weight-light mb-3"><fmt:message
                                key="content.page.profile.favourite.noneyet"/></h4>
                    </div>

                    <div class="row">
                        <%--CARDS--%>
                        <div class="card float-left m-2" style="width: 16rem;"
                             dir-paginate="card in prof.cocktails | itemsPerPage: prof.showPages track by $index">
                            <img class="card-img-top" src="${ pageContext.request.contextPath }{{card.uri}}"
                                 alt="Card image cap">
                            <%--BODY--%>
                            <div class="card-body">
                                <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                                <p class="card-text" style="height: 50px; overflow: auto;">
                            <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}<span
                                    ng-if="!$last">, </span></span>
                                </p>
                                <a href="/controller?command=cocktail&id={{card.id}}"
                                   class="btn btn-primary"><fmt:message
                                        key="content.pagination.view"/></a>
                                <a href="#" ng-click="prof.unlike(card.id)" class="btn btn-danger ml-3"><fmt:message
                                        key="content.button.unlike"/></a>
                            </div>
                        </div>
                    </div>

                    <div class="row m-3" ng-hide="prof.cocktails.length <= 12">
                        <div class="btn-toolbar" id="btngroup" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group mr-2" role="group" aria-label="First group">
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(12)">
                                    12
                                </button>
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(24)">
                                    24
                                </button>
                                <button type="button" class="btn btn-secondary" ng-click="prof.changeDisplayNumber(48)">
                                    48
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="row m-3">
                        <%--paginaton. change default template classes to bootstrap 4--%>
                        <dir-pagination-controls boundary-links="true"
                                                 template-url="${ pageContext.request.contextPath }/js/dirPagination.tpl.html">
                        </dir-pagination-controls>
                    </div>

                </div>
            </div>

            <div class="tab-pane fade" id="myContent" role="tabpanel" aria-labelledby="newCocktail"></div>

            <%--CREATE COCKTAIL--%>
            <div class="tab-pane fade" id="newContent" role="tabpanel" aria-labelledby="myCocktails">
                <div class="container" style="width: 100%;">
                    <%--NECESSARY--%>
                    <div class="form-group mb-3">
                        <small class="form-text text-muted"><fmt:message key="create.necessary"/></small>
                        <small class="form-text text-muted"><fmt:message key="create.howto"/></small>
                    </div>

                    <%--IMAGE--%>
                    <div class="form-group mb-3" id="imgDiv" ng-show="prof.pictureVisible">
                        <img src="${ pageContext.request.contextPath }{{prof.selectedBaseCocktail.uri}}"
                             class="img-fluid img-thumbnail"
                             id="cocktailImage"
                             width="286px"
                             height="180px"
                             alt="<fmt:message key="create.image.choose"/>">
                    </div>

                    <%--CHOOSE IMAGE--%>
                    <div class="form-group mb-3">
                        <div class="row">
                            <div class="col" id="uploadImgDiv">
                                <div class="form-group">
                                    <input type="file"
                                           class="form-control-file"
                                           id="uploadImage"
                                           file-change="prof.upload($event)"
                                           ng-model="prof.file"
                                           accept="image/jpeg,image/png,image/gif">
                                    <label for="uploadImage" class="text-muted"><fmt:message
                                            key="create.image.choose"/></label>
                                    <small ng-show="prof.msgInvalidSize" class="form-text text-danger"><fmt:message key="create.toobigimg"/></small>
                                </div>
                            </div>
                            <div class="col">
                                <button type="button"
                                        class="btn btn-warning"
                                        ng-click="prof.removeImage()"><fmt:message
                                        key="create.ingredients.removeImage"/>
                                </button>
                            </div>
                        </div>

                    </div>

                    <%--TYPE & BASE DRINK & BASE COCKTAIL--%>
                    <div class="form-group mb-3">
                        <div class="row">
                            <div class="col">
                                <%--BASE COCKTAIL--%>
                                <div class="form-group">
                                    <label for="baseCocktail"><fmt:message key="create.basecocktail"/></label>
                                    <select class="form-control" id="baseCocktail">
                                        <option selected ng-click="prof.setBaseCocktail(0)"><fmt:message
                                                key="create.choose"/></option>
                                        <option ng-repeat="cocktail in prof.catalogCocktails"
                                                ng-click="prof.setBaseCocktail(cocktail.id)">{{cocktail.name}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="col">
                                <%--TYPE--%>
                                <div class="form-group">
                                    <label for="drinkTypeDropList"><fmt:message key="create.type"/></label>
                                    <select class="form-control" id="drinkTypeDropList" required>
                                        <option ng-repeat="type in prof.drinkTypes"
                                                ng-model="prof.selectedBaseCocktail.type"
                                                value="{{type}}">{{type}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div class="col">
                                <%--BASE DRINK--%>
                                <div class="form-group">
                                    <label for="baseDrinkDropList"><fmt:message key="create.base"/></label>
                                    <select class="form-control" id="baseDrinkDropList" required>
                                        <option ng-repeat="base in prof.baseDrinks"
                                                ng-model="prof.selectedBaseCocktail.baseDrink"
                                                value="{{base}}">{{base}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%--NAME--%>
                    <div class="form-group mb-3">
                        <label for="createName"><fmt:message key="create.name"/></label>
                        <input type="text"
                               class="form-control"
                               id="createName"
                               maxlength="60"
                               ng-model="prof.selectedBaseCocktail.name"
                               ng-change="prof.updateName()"
                               placeholder="<fmt:message key="create.name.placeholder"/>"
                               required
                               autofocus>
                        <small class="form-text text-muted"><fmt:message
                                key="create.charsleft"/>{{prof.nameLeft}}
                        </small>
                    </div>

                    <%--SLOGAN--%>
                    <div class="form-group mb-3">
                        <label for="createSlogan"><fmt:message key="create.slogan"/></label>
                        <input type="text"
                               class="form-control"
                               id="createSlogan"
                               ng-model="prof.selectedBaseCocktail.slogan"
                               ng-change="prof.updateSlogan()"
                               maxlength="255"
                               placeholder="<fmt:message key="create.slogan.placeholder"/>">
                        <small class="form-text text-muted"><fmt:message
                                key="create.charsleft"/>{{prof.sloganLeft}}
                        </small>
                    </div>

                    <%--INGREDIENTS--%>
                    <div class="form-group mb-3">
                        <div class="row">
                            <div class="col">
                                <button type="button"
                                        class="btn btn-info"
                                        ng-click="prof.addPortion()"><fmt:message
                                        key="create.ingredients.add"/>
                                </button>
                                <small class="form-text text-muted"><fmt:message
                                        key="create.ingredients.restrition"/> {{prof.ingredientsLeft}}
                                </small>
                            </div>
                            <div class="col">
                                <button type="button"
                                        class="btn btn-warning"
                                        ng-click="prof.cleanIngredients()"><fmt:message
                                        key="create.ingredients.clean"/>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="form-group mb-3"
                         ng-repeat="portion in prof.selectedBaseCocktail.ingredientList track by $index">
                        <div class="row">
                            <div class="col">
                                <label for="ingredientSelect{{$index}}"><fmt:message
                                        key="create.ingredients.name"/></label>
                                <select class="form-control"
                                        id="ingredientSelect{{$index}}"
                                        ng-options="ingredient for ingredient in prof.ingredients"
                                        ng-model="portion.ingredientName"
                                        required>
                                </select>
                            </div>
                            <div class="col">
                                <label for="enterPortion"><fmt:message key="create.ingredients.portion"/></label>
                                <input type="text"
                                       class="form-control"
                                       id="enterPortion"
                                       maxlength="50"
                                       ng-model="portion.amount"
                                       ng-change="prof.updatePortion($index)"
                                       placeholder="<fmt:message key="create.ingredients.portion.placeholder"/>"
                                       required>
                                <small class="form-text text-muted"><fmt:message
                                        key="create.charsleft"/>{{prof.portionLeftArr[$index]}}
                                </small>
                            </div>
                            <div class="col" style="margin-top: 32px;">
                                <button type="button"
                                        class="btn btn-danger"
                                        ng-click="prof.removePortion($index)"><fmt:message
                                        key="create.remove"/>
                                </button>
                            </div>
                        </div>
                    </div>

                    <%--RECIPE--%>
                    <div class="form-group mb-3">
                        <label for="recipe"><fmt:message key="create.recipe"/></label>
                        <textarea class="form-control"
                                  id="recipe"
                                  rows="3"
                                  maxlength="1000"
                                  ng-model="prof.selectedBaseCocktail.recipe"
                                  ng-change="prof.updateTextarea()"
                                  placeholder="<fmt:message key="create.recipe.placeholder"/>"
                                  required></textarea>
                        <small class="form-text text-muted"><fmt:message
                                key="create.charsleft"/>{{prof.textAreaLeft}}
                        </small>
                    </div>

                    <%--SAVE BUTTON--%>
                    <div class="form-group mb-3">
                        <button type="button"
                                ng-click="prof.saveCocktail()"
                                class="btn btn-primary"><fmt:message
                                key="create.save"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>