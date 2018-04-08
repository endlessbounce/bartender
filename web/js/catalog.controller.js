(function () {
    angular.module("catalog")
        .controller("CatalogCtrl", function (restService) {
            var self = this;
            self.editMode = false;

            //button group show pages
            self.showPages = 8;

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //list of cocktails
            self.todos = [];

            restService.getFormData().then(function (data) {
                console.log("response data: " + data);
                self.baseDrinks = data.baseDrink;
                self.drinkTypes = data.drinkType;
                self.ingredients = data.ingredient;
            });

            self.displayNumber = function (number) {
                this.showPages = number;
            }

            self.getBaseDrinks = function () {

            }

            self.getDrinkTypes = function () {

            }

            self.getIngredients = function () {

            }

            self.getCocktails = function () {

            }
        });
})();