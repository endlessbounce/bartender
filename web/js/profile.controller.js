(function () {
    angular.module("catalog")
        .controller("ProfileCtrl", function (restService) {
            var self = this;
            self.userID = null;

            /**
             * Is called on initialization of profile page
             * @param section - if not null, 'user cocktails' section will be active
             * @param userID - user's ID
             */
            self.init = function (section, userID) {
                self.userID = userID;
                console.log("ProfileCtrl ON_INIT: userID: " + userID + ", section: " + section);

                //getAllFavourite & getAllCreated are called in on-init method because they use userID,
                //and outside of this method it will be none before on-init gets userID injected
                //get favourite cocktails on load of the page
                restService.getAllFavourite(self.userID).then(function (data) {
                    self.cocktails = data;
                    console.log('getting all favourite ' + data)
                    document.getElementById("firstItem").style.visibility = "visible";
                });

                //get all created by user cocktails on load of the page
                restService.getAllCreated(self.userID).then(function (data) {
                    self.createdCocktails = data;
                    console.log('getting all created ' + data)
                    document.getElementById("secondItem").style.visibility = "visible";
                });

                //check if user was moving from a page of created by him cocktail
                if(section == 'created'){
                    document.getElementById("favourite").classList.remove('active');
                    document.getElementById("favContent").classList.remove('show');
                    document.getElementById("favContent").classList.remove('active');
                    document.getElementById("myCocktails").classList.add('active');
                    document.getElementById("myContent").classList.add('show');
                    document.getElementById("myContent").classList.add('active');
                }
            }

            //button group show pages
            self.showPages = 12;

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //list of favourite cocktails
            self.cocktails = [];
            self.createdCocktails = [];
            self.catalogCocktails = [];
            //ngModel selected cocktail (is edited and sent to the server)
            self.selectedBaseCocktail = {
                "name": "",
                "recipe": "",
                "baseDrink": "",
                "type": "",
                "uri": "",
                "slogan": "",
                "ingredientList": []
            };

            //restrictions
            self.textAreaLeft = 1000;
            self.nameLeft = 60;
            self.sloganLeft = 255;
            self.ingredientsLeft = 20;
            //max 20 portions
            self.portionLeftArr = [];

            var l;
            for (l = 0; l < 20; l++) {
                self.portionLeftArr.push(50);//50 characters for amount
            }

            self.pictureVisible = false;
            self.pictureValidSize = true;
            self.msgInvalidSize = false;
            self.notEnoughIngredients = true;
            self.typeIsNotChosen = true;
            self.baseIsNotChosen = true;
            self.ingredientIsChosen = true;
            self.errorNotSaved = true;
            self.ingredientsUnique = true;
            self.editMode = false;

            // ****************************************************
            // ****************FAVOURITE***************************
            // ****************************************************

            /*change number of displayed cocktails*/
            self.changeDisplayNumber = function (number) {
                self.showPages = number;
            };

            /**
             * Deletes favourite cocktail from the DB
             * @param cocktailID
             */
            self.unlike = function (cocktailID) {
                console.log("unliked: " + cocktailID);
                restService.deleteLiked(cocktailID, self.userID).then(function () {

                    restService.getAllFavourite(self.userID).then(function (data) {
                        console.log("favourite cocktails: " + data);
                        self.cocktails = data;
                    });

                });
            }

            // ****************************************************
            // ****************CREATE COCKTAIL*********************
            // ****************************************************
            //get all cocktails for base cocktail droplist
            restService.getCocktails().then(function (data) {
                console.log("getCocktails: " + data);
                self.catalogCocktails = data;
            });

            //get form data on load of the page
            restService.getFormData().then(function (data) {
                console.log("getFormData data: " + data);
                self.baseDrinks = data.baseDrink;
                self.drinkTypes = data.drinkType;
                self.ingredients = data.ingredient;
            });

            self.setBaseCocktail = function (baseCocktailID) {
                if (baseCocktailID != 0) {
                    self.selectedBaseCocktail.ingredientList = [];

                    var i;
                    for (i = 0; i < self.catalogCocktails.length; i++) {
                        if (self.catalogCocktails[i].id == baseCocktailID) {

                            //find base cocktail and push the copy of its ingredient list into the cocktail being built
                            var k;
                            for (k = 0; k < self.catalogCocktails[i].ingredientList.length; k++) {
                                var ingName = self.catalogCocktails[i].ingredientList[k].ingredientName;
                                var amount = self.catalogCocktails[i].ingredientList[k].amount;

                                self.selectedBaseCocktail.ingredientList.push({
                                    "ingredientName": ingName,
                                    "amount": amount
                                });
                            }

                            //update number of characters left for amounts
                            var j;
                            for (j = 0; j < self.selectedBaseCocktail.ingredientList.length; j++) {
                                if (self.selectedBaseCocktail.ingredientList[j].amount == undefined) {
                                    self.selectedBaseCocktail.ingredientList[j].amount = "";
                                }
                                self.portionLeftArr[j] = 50 - self.selectedBaseCocktail.ingredientList[j].amount.length;
                            }

                            //update number of ingredients left
                            self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;

                            self.selectedBaseCocktail.uri = self.catalogCocktails[i].uri;
                            self.pictureVisible = true;
                            self.pictureValidSize = true;
                            self.msgInvalidSize = false;
                        }
                    }
                } else {
                    self.selectedBaseCocktail = {
                        "name": "",
                        "recipe": "",
                        "baseDrink": "",
                        "type": "",
                        "uri": "",
                        "slogan": "",
                        "ingredientList": []
                    };
                    self.ingredientsLeft = 20;
                    document.getElementById('uploadImage').value = '';
                    self.pictureVisible = false;
                    self.pictureValidSize = false;
                    self.msgInvalidSize = false;
                }
            }

            self.updateTextarea = function () {
                if (self.selectedBaseCocktail.recipe != undefined) {
                    self.textAreaLeft = 1000 - self.selectedBaseCocktail.recipe.length;
                } else {
                    self.textAreaLeft = 1000;
                }
            }

            self.updateName = function () {
                if (self.selectedBaseCocktail.name != undefined) {
                    self.nameLeft = 60 - self.selectedBaseCocktail.name.length;
                } else {
                    self.nameLeft = 60;
                }
            }

            self.updateSlogan = function () {
                if (self.selectedBaseCocktail.slogan != undefined) {
                    self.sloganLeft = 255 - self.selectedBaseCocktail.slogan.length;
                } else {
                    self.sloganLeft = 255;
                }
            }

            self.updatePortion = function (index) {
                if (self.selectedBaseCocktail.ingredientList[index].amount != undefined) {
                    self.portionLeftArr[index] = 50 - self.selectedBaseCocktail.ingredientList[index].amount.length;
                } else {
                    self.portionLeftArr[index] = 50;
                }
            }

            self.removePortion = function (index) {
                self.selectedBaseCocktail.ingredientList.splice(index, 1);
                self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;
            }

            self.addPortion = function () {
                if (self.selectedBaseCocktail.ingredientList.length < 20) {
                    self.selectedBaseCocktail.ingredientList.push({
                        "ingredientName": "",
                        "amount": ""
                    });
                }
                self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;

                //remove message of minimum 2 ingredients
                if (self.ingredientsLeft <= 18) {
                    self.notEnoughIngredients = true;
                }
            }

            self.cleanIngredients = function () {
                self.selectedBaseCocktail.ingredientList = [];
                self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;
            }

            self.saveCocktail = function () {
                var length = self.selectedBaseCocktail.ingredientList.length;
                if (length < 2) {
                    //show a message that there should be at least 2 ingredients
                    self.notEnoughIngredients = false;
                    return;
                } else if (self.selectedBaseCocktail.type == '') {
                    self.typeIsNotChosen = false;
                    return;
                } else if (self.selectedBaseCocktail.baseDrink == '') {
                    self.baseIsNotChosen = false;
                    return;
                } else {
                    self.checkIngredients();
                }

                if (self.pictureValidSize &&
                    self.ingredientsUnique &&
                    self.ingredientIsChosen) {
                    console.log(JSON.stringify(self.selectedBaseCocktail));

                    //send data to the server
                    var promise;

                    if(self.editMode){
                        promise = restService.updateCreated(self.selectedBaseCocktail, self.userID);
                    }else{
                        promise = restService.addCreated(self.selectedBaseCocktail, self.userID);
                    }

                    promise.then(function () {
                            //update list of created cocktails
                            restService.getAllCreated(self.userID).then(function (data) {
                                self.createdCocktails = data;
                                document.getElementById("secondItem").style.visibility = "visible";
                            });

                            //cleaning of the section
                            self.cleanCreateSection();

                            //moving view to "my cocktails" pill
                            document.getElementById("myCocktails").classList.add('active');
                            document.getElementById("myContent").classList.add('show');
                            document.getElementById("myContent").classList.add('active');

                        }, function (data) {
                            //show a message if an error happened on server side
                            console.log(data);
                            self.errorNotSaved = false;
                            setTimeout(function () {
                                self.errorNotSaved = true;
                            }, 5000);
                        });
                }
            }

            self.upload = function () {
                var input = document.getElementById('uploadImage');

                //self.file is an ng-Model return value after upload from the input tag
                if (self.file.size < 500000) {
                    var reader = new FileReader();

                    reader.onload = function () {
                        document.getElementById('cocktailImage').src = reader.result;
                        self.pictureVisible = true;
                        self.selectedBaseCocktail.uri = reader.result;
                        console.log(reader.result);
                    }

                    reader.readAsDataURL(input.files[0]);

                    console.log(self.file.name + " size: " + self.file.size);

                    self.pictureValidSize = true;
                    self.msgInvalidSize = false;
                } else {
                    input.value = '';
                    self.pictureValidSize = false;
                    self.msgInvalidSize = true;
                }
            }

            self.removeImage = function () {
                self.selectedBaseCocktail.uri = "";
                self.pictureValidSize = true;
                self.msgInvalidSize = false;
                self.pictureVisible = false;
                document.getElementById('uploadImage').value = '';
                document.getElementById('cocktailImage').src = '';
            }

            self.checkDrinkType = function () {
                if (self.selectedBaseCocktail.type != '') {
                    self.typeIsNotChosen = true;
                }
            }

            self.checkBaseDrink = function () {
                if (self.selectedBaseCocktail.baseDrink != '') {
                    self.baseIsNotChosen = true;
                }
            }

            self.checkIngredients = function () {
                self.checkAllSelected();
                self.checkUniqueIngredients();
            }

            self.checkAllSelected = function () {
                self.ingredientIsChosen = true;
                var length = self.selectedBaseCocktail.ingredientList.length;
                var p;
                for (p = 0; p < length; p++) {
                    if (self.selectedBaseCocktail.ingredientList[p].ingredientName == '') {
                        self.ingredientIsChosen = false;
                    }
                }
            }

            self.checkUniqueIngredients = function () {
                self.ingredientsUnique = true;
                var i, j, length = self.selectedBaseCocktail.ingredientList.length;

                if (length >= 2) {
                    for (i = 0; i < length - 1; i++) {
                        for (j = i + 1; j < length; j++) {

                            if (self.selectedBaseCocktail.ingredientList[i].ingredientName ==
                                self.selectedBaseCocktail.ingredientList[j].ingredientName) {

                                self.ingredientsUnique = false;
                            }
                        }
                    }
                }
            }

            self.cleanCreateSection = function () {
                if(self.editMode){
                    document.getElementById("editCocktail").classList.remove('active');
                }else{
                    document.getElementById("newCocktail").classList.remove('active');
                }

                document.getElementById("newContent").classList.remove('show');
                document.getElementById("newContent").classList.remove('active');
                self.cleanFrom();
            }

            self.cleanFrom = function () {
                self.selectedBaseCocktail = {
                    "name": "",
                    "recipe": "",
                    "baseDrink": "",
                    "type": "",
                    "uri": "",
                    "slogan": "",
                    "ingredientList": []
                };
                self.removeImage();
                self.textAreaLeft = 1000;
                self.nameLeft = 60;
                self.sloganLeft = 255;
                self.ingredientsLeft = 20;
                self.portionLeftArr = [];
                var l;
                for (l = 0; l < 20; l++) {
                    self.portionLeftArr.push(50);//50 characters for amount
                }
            }

            self.deleteCreated = function (cocktailID) {
                console.log("deleting: " + cocktailID);
                restService.deleteCreatedCocktail(cocktailID, self.userID).then(function () {

                        restService.getAllCreated(self.userID).then(function (data) {
                            console.log("updating created cocktails: " + data);
                            self.createdCocktails = data;
                        });

                    });
            }

            self.editCreated = function (cocktailID) {
                document.getElementById("editCocktail").style.visibility = "visible";
                document.getElementById("updateButton").style.visibility = "visible";
                var i;
                for(i = 0; i < self.createdCocktails.length; i++){
                    if(self.createdCocktails[i].id == cocktailID){
                        console.log("editing cocktail: " + JSON.stringify(self.createdCocktails[i]));
                        self.selectedBaseCocktail = self.createdCocktails[i];
                        document.getElementById('cocktailImage').src = self.selectedBaseCocktail.uri;
                        self.pictureVisible = true;
                    }
                }

                self.updateName();
                self.updateSlogan();
                self.updateTextarea();
                var ingredients = self.selectedBaseCocktail.ingredientList.length;
                for(i = 0; i < ingredients; i++){
                    self.updatePortion(i);
                }
                self.ingredientsLeft = 20 - ingredients;

                document.getElementById("myCocktails").classList.remove('active');
                document.getElementById("myContent").classList.remove('show');
                document.getElementById("myContent").classList.remove('active');
                document.getElementById("editCocktail").classList.add('active');
                document.getElementById("newContent").classList.add('show');
                document.getElementById("newContent").classList.add('active');
                self.editMode = true;
            }
        })//directive for binding file input and model. this allows to use ng-change and dynamically
        //display new uploaded image
        .directive('fileChange', function () {
            return {
                restrict: 'A',
                require: 'ngModel',
                scope: {
                    fileChange: '&'
                },
                link: function link(scope, element, attrs, ctrl) {
                    element.on('change', onChange);

                    scope.$on('destroy', function () {
                        element.off('change', onChange);
                    });

                    function onChange() {
                        attrs.multiple ? ctrl.$setViewValue(element[0].files) : ctrl.$setViewValue(element[0].files[0]);
                        scope.$apply(function () {
                            scope.fileChange();
                        });
                    }
                }
            };
        });
})();
