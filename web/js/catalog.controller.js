(function () {
    angular.module("catalog")
        .controller("CatalogCtrl", function (restService) {
            var self = this;
            self.userUri = "http://localhost:8080/webapi/cocktails";

            //button group show pages
            self.showPages = 12;

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //list of cocktails
            self.cocktails = [];

            //chosen parameters
            self.params = {
                'currentDrinkType': null,
                'currentBaseDrink': null,
                'currentIngredients': []
            }

            //get form data on load of the page
            restService.getFormData().then(function (data) {
                console.log("getFormData data: " + data);
                self.baseDrinks = data.baseDrink;
                self.drinkTypes = data.drinkType;
                self.ingredients = data.ingredient;
            });

            //get all cocktails on load of the page
            restService.getCocktails().then(function (data) {
                console.log("getCocktails data: " + data);
                self.cocktails = data;
                var onLoad = document.getElementById("onLoad");
                if(onLoad){
                    onLoad.style.visibility = "visible";
                }
            });

            /*change number of displayed cocktails*/
            self.changeDisplayNumber = function (number) {
                self.showPages = number;
            };


            //******************************************************************************
            //FORM ELEMENTS FUNCTIONS
            //******************************************************************************

            //triggers when a user chooses a type of a drink from the dropdown list
            self.addDrinkType = function (event) {
                var value = event.target.value;
                console.log("chosen drink type: " + value);

                if (value == "delete") {
                    self.params.currentDrinkType = null;
                } else {
                    self.params.currentDrinkType = value;
                }

                var newPath = self.buildRecourse();
                restService.getCocktailsWithParams(newPath).then(function (data) {
                    console.log("addDrinkType data: " + data);
                    self.cocktails = data;
                });
            }

            //triggers when a user chooses a base drink from the dropdown list
            self.addBaseDrink = function (event) {
                var value = event.target.value;
                console.log("chosen base drink: " + value);

                if (value == "delete") {
                    self.params.currentBaseDrink = null;
                } else {
                    self.params.currentBaseDrink = value;
                }

                var newPath = self.buildRecourse();
                restService.getCocktailsWithParams(newPath).then(function (data) {
                    console.log("addBaseDrink data: " + data);
                    self.cocktails = data;
                });
            }

            //triggers when user picks out some desired ingredients from the checkbox
            self.addIngredient = function (event) {
                var ingredient = event.target.id;
                var isChosen = document.getElementById(ingredient).checked;
                console.log("add ingredient - value:" + isChosen + " , chosen ingredient: " + ingredient);

                if (isChosen == true) {
                    self.params.currentIngredients.push(ingredient);
                    console.log("added: " + ingredient);
                } else {
                    var size = self.params.currentIngredients.length;
                    console.log("size: " + size);
                    var i;
                    for (i = 0; i < size; i++) {
                        if (self.params.currentIngredients[i] == ingredient) {
                            self.params.currentIngredients.splice(i, 1);
                            console.log("deleted: " + ingredient);
                        }
                    }
                }

                var newPath = self.buildRecourse();
                restService.getCocktailsWithParams(newPath).then(function (data) {
                    console.log("addIngredient data: " + data);
                    self.cocktails = data;
                });
            }

            //reset filtering form
            self.reset = function () {
                var j;
                for (j = 0; j < self.params.currentIngredients.length; j++) {
                    var ingredient = self.params.currentIngredients[j];
                    document.getElementById(ingredient).checked = false;
                }
                self.params.currentIngredients = [];

                document.getElementById("drinkType").selectedIndex = 0;
                self.params.currentDrinkType = null;
                document.getElementById("baseDrink").selectedIndex = 0;
                self.params.currentBaseDrink = null;

                restService.getCocktails().then(function (data) {
                    console.log("getCocktails data: " + data);
                    self.cocktails = data;
                });
            }

            //******************************************************************************
            //BUILD RECOURSE PATH
            //******************************************************************************
            self.buildRecourse = function () {
                var buildPath = self.userUri + "?";
                var type = self.params.currentDrinkType;
                var base = self.params.currentBaseDrink;
                var ingredientsNum = self.params.currentIngredients.length;

                console.log("type: " + type + ", base: " + base + ", ingreds: " + self.params.currentIngredients);

                if (type != null) {
                    buildPath += "type=" + type;
                }

                if (base != null && type != null) {
                    buildPath += "&base=" + base;
                } else if (base != null) {
                    buildPath += "base=" + base;
                }

                if (ingredientsNum > 0) {
                    var j;
                    for (j = 0; j < ingredientsNum; j++) {
                        if (j == 0 && base == null && type == null) {
                            buildPath += "ingredient" + (j + 1) +
                                "=" + self.params.currentIngredients[j];
                        } else {
                            buildPath += "&ingredient" + (j + 1) +
                                "=" + self.params.currentIngredients[j];
                        }
                    }
                }

                console.log("recourse path: " + buildPath);
                return buildPath;
            }


        });
})();