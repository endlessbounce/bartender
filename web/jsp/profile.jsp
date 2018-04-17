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
            <a class="nav-link active"
               id="favourite"
               data-toggle="pill"
               href="#favContent"
               role="tab"
               ng-click="prof.cleanCreateSection()"
               aria-controls="favContent"
               aria-selected="true"><fmt:message key="content.page.profile.favourite"/>
            </a>
            <a class="nav-link"
               id="myCocktails"
               data-toggle="pill"
               href="#myContent"
               role="tab"
               ng-click="prof.cleanCreateSection()"
               aria-controls="myContent"
               aria-selected="false"><fmt:message key="content.page.profile.mycocktails"/>
            </a>
            <a class="nav-link"
               id="newCocktail"
               data-toggle="pill"
               href="#newContent"
               role="tab"
               aria-controls="newContent"
               aria-selected="false"><fmt:message key="content.page.profile.create"/>
            </a>
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
                             dir-paginate="card in prof.cocktails | itemsPerPage: prof.showPages track by $index"
                             pagination-id="first">
                            <img class="card-img-top"
                                 ng-src="${ pageContext.request.contextPath }{{card.uri}}"
                                 alt="Card image cap">
                            <%--BODY--%>
                            <div class="card-body">
                                <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                                <p class="card-text" style="height: 50px; overflow: auto;">
                            <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}<span
                                    ng-if="!$last">, </span></span>
                                </p>
                                <a ng-href="/controller?command=cocktail&id={{card.id}}"
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
                                                 pagination-id="first"
                                                 template-url="${ pageContext.request.contextPath }/js/dirPagination.tpl.html">
                        </dir-pagination-controls>
                    </div>

                </div>
            </div>

            <%--USER'S CREATED COCKTAILS--%>
            <div class="tab-pane fade" id="myContent" role="tabpanel" aria-labelledby="newCocktail">
                <div id="secondItem" style="visibility: hidden;">

                    <div class="row" ng-if="prof.createdCocktails.length == 0">
                        <h4 class="font-weight-light mb-3"><fmt:message
                                key="content.page.profile.created.noneyet"/></h4>
                    </div>

                    <div class="row">
                        <%--CARDS--%>
                        <div class="card float-left m-2" style="width: 16rem;"
                             dir-paginate="card in prof.createdCocktails | orderBy : 'id' : reverse | itemsPerPage: prof.showPages"
                             pagination-id="second">
                            <img class="card-img-top"
                                 height="159.85px"
                                 width="254px"
                                 ng-src="${ pageContext.request.contextPath }{{card.uri}}"
                                 alt="Card image cap">
                            <%--BODY--%>
                            <div class="card-body">
                                <h6 class="card-title text-truncate" ng-bind="card.name"></h6>
                                <p class="card-text" style="height: 50px; overflow: auto;">
                                    <span ng-repeat="portion in card.ingredientList">{{portion.ingredientName}}
                                        <span ng-if="!$last">, </span>
                                    </span>
                                </p>
                                <a ng-href="/controller?command=user_cocktail&id={{card.id}}"
                                   class="btn btn-primary"><fmt:message
                                        key="content.pagination.view"/>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="row m-3" ng-hide="prof.createdCocktails.length <= 12">
                        <div class="btn-toolbar" id="btngroupCreated" role="toolbar" aria-label="Toolbar with button groups">
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
                                                 pagination-id="second"
                                                 template-url="${ pageContext.request.contextPath }/js/dirPagination.tpl.html">
                        </dir-pagination-controls>
                    </div>

                </div>
            </div>

            <%--CREATE COCKTAIL--%>
            <div class="tab-pane fade" id="newContent" role="tabpanel" aria-labelledby="myCocktails">
                <div class="container" style="width: 100%;">
                    <form ng-submit="prof.saveCocktail()">
                        <%--NECESSARY--%>
                        <div class="form-group mb-3">
                            <small class="form-text text-muted"><fmt:message key="create.necessary"/></small>
                            <small class="form-text text-muted"><fmt:message key="create.howto"/></small>
                        </div>

                        <%--IMAGE--%>
                        <div class="form-group mb-3" id="imgDiv" ng-show="prof.pictureVisible">
                            <img ng-src="${ pageContext.request.contextPath }{{prof.selectedBaseCocktail.uri}}"
                                 class="img-fluid img-thumbnail"
                                 id="cocktailImage"
                                 height="159.85px"
                                 width="254px"
                                 alt="<fmt:message key="create.image.choose"/>">
                        </div>

                        <%--CHOOSE IMAGE--%>
                        <div class="form-group mb-3">
                            <div class="row">
                                <div class="col" id="uploadImgDiv">
                                    <div class="form-group">
                                        <label for="uploadImage"><fmt:message
                                                key="create.image.choose"/></label>
                                        <input type="file"
                                               class="form-control-file"
                                               id="uploadImage"
                                               file-change="prof.upload($event)"
                                               ng-model="prof.file"
                                               value="<fmt:message key="create.image.choose.img"/>"
                                               accept="image/jpeg,image/png,image/gif">
                                        <small class="form-text text-muted"><fmt:message
                                                key="create.image.choose.hint"/></small>
                                        <small ng-show="prof.msgInvalidSize" class="form-text text-danger"><fmt:message
                                                key="create.toobigimg"/></small>
                                    </div>
                                </div>
                                <div class="col">

                                </div>
                                <div class="col">
                                    <button type="button"
                                            class="btn btn-warning"
                                            ng-click="prof.removeImage()"><fmt:message
                                            key="create.ingredients.removeImage"/>
                                    </button>
                                    <button type="button"
                                            class="btn btn-warning"
                                            ng-click="prof.cleanFrom()"><fmt:message
                                            key="create.ingredients.cleanform"/>
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
                                        <select class="form-control"
                                                id="drinkTypeDropList"
                                                ng-options="type for type in prof.drinkTypes"
                                                ng-model="prof.selectedBaseCocktail.type"
                                                ng-change="prof.checkDrinkType()"
                                                required>
                                        </select>
                                        <small ng-hide="prof.typeIsNotChosen" class="form-text text-danger"><fmt:message
                                                key="create.typeisnotchosen"/></small>
                                    </div>
                                </div>
                                <div class="col">
                                    <%--BASE DRINK--%>
                                    <div class="form-group">
                                        <label for="baseDrinkDropList"><fmt:message key="create.base"/></label>
                                        <select class="form-control"
                                                id="baseDrinkDropList"
                                                ng-options="base for base in prof.baseDrinks"
                                                ng-model="prof.selectedBaseCocktail.baseDrink"
                                                ng-change="prof.checkBaseDrink()"
                                                required>
                                        </select>
                                        <small ng-hide="prof.baseIsNotChosen" class="form-text text-danger"><fmt:message
                                                key="create.baseisnotchosen"/></small>
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
                                    <small ng-hide="prof.notEnoughIngredients" class="form-text text-danger">
                                        <fmt:message
                                                key="create.minimumingredients"/></small>
                                </div>
                                <div class="col">

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
                                            ng-change="prof.checkIngredients()"
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
                            <button type="submit" class="btn btn-primary"><fmt:message
                                    key="create.save"/>
                            </button>
                            <small ng-hide="prof.ingredientIsChosen" class="form-text text-danger"><fmt:message
                                    key="create.ingredientisnotchosen"/></small>
                            <small ng-hide="prof.errorNotSaved" class="form-text text-danger"><fmt:message
                                    key="create.error"/></small>
                            <small ng-hide="prof.ingredientsUnique" class="form-text text-danger"><fmt:message
                                    key="create.unique"/></small>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../WEB-INF/jspf/footer.jspf" %>
</body>
</html>