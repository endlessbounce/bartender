(function () {
    angular.module("catalog")
        .controller("ProfileCtrl", function (restService) {
            var self = this;
            self.userID = document.getElementById("userID").textContent;

            //button group show pages
            self.showPages = 12;

            //list of favourite cocktails
            self.cocktails = [];
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

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //restrictions
            self.textAreaLeft = 1000;
            self.nameLeft = 60;
            self.sloganLeft = 255;
            self.ingredientsLeft = 20;
            //max 20 portions
            self.portionLeftArr = [];

            var l;
            for (l = 0; l < 20; l++) {
                self.portionLeftArr.push(50);
            }

            self.pictureVisible = false;

            console.log("profile controller user id: " + self.userID);

            // ****************************************************
            // ****************FAVOURITE***************************
            // ****************************************************

            //get favourite cocktails on load of the page
            restService.getAllFavourite(self.userID).then(function (data) {
                console.log("favourite cocktails: " + data);
                self.cocktails = data;
                document.getElementById("firstItem").style.visibility = "visible";
            });

            /*change number of displayed cocktails*/
            self.changeDisplayNumber = function (number) {
                self.showPages = number;
            };

            self.unlike = function (cocktailID) {
                console.log("unliked: " + cocktailID);
                restService.deleteLiked(cocktailID, self.userID);
                restService.getAllFavourite(self.userID).then(function (data) {
                    console.log("favourite cocktails: " + data);
                    self.cocktails = data;
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
                                if(self.selectedBaseCocktail.ingredientList[j].amount == undefined){
                                    self.selectedBaseCocktail.ingredientList[j].amount = "";
                                }
                                self.portionLeftArr[j] = 50 - self.selectedBaseCocktail.ingredientList[j].amount.length;
                            }

                            //update number of characters left for ingredients
                            self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;

                            self.selectedBaseCocktail.uri = self.catalogCocktails[i].uri;
                            self.pictureVisible = true;
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
                    self.pictureVisible = false;
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
            }

            self.cleanIngredients = function () {
                self.selectedBaseCocktail.ingredientList = [];
                self.ingredientsLeft = 20 - self.selectedBaseCocktail.ingredientList.length;
            }
        });
})();