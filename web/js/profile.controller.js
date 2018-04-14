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
            self.selectedBaseCocktail = {};

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //restrictions
            self.textAreaLeft = 1000;
            self.nameLeft = 60;
            self.sloganLeft = 255;
            self.portionLeft = 50;


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
                    var i;
                    for (i = 0; i < self.catalogCocktails.length; i++) {
                        if (self.catalogCocktails[i].id == baseCocktailID) {
                            self.selectedBaseCocktail.name = self.catalogCocktails[i].name;
                            self.selectedBaseCocktail.ingredientList = self.catalogCocktails[i].ingredientList;
                            self.selectedBaseCocktail.uri = self.catalogCocktails[i].uri;

                            self.nameLeft = 60 - self.selectedBaseCocktail.name.length;
                        }
                    }
                } else {
                    self.selectedBaseCocktail = {};
                }
            }

            self.setPortion = function (index) {

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
                    self.portionLeft = 50 - self.selectedBaseCocktail.ingredientList[index].amount.length;
                } else {
                    self.portionLeft = 50;
                }
            }
            
            self.removePortion = function () {

            }
        });
})();