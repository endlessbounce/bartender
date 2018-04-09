(function () {
    angular.module("catalog")
        .controller("CatalogCtrl", function (restService) {
            var self = this;
            self.editMode = false;

            //button group show pages
            self.showPages = 12;

            //from data
            self.baseDrinks = [];
            self.drinkTypes = [];
            self.ingredients = [];

            //list of cocktails
            self.cocktails = [];

            restService.getFormData().then(function (data) {
                console.log("response data: " + data);
                self.baseDrinks = data.baseDrink;
                self.drinkTypes = data.drinkType;
                self.ingredients = data.ingredient;
            });

            restService.getCocktails(0, self.showPages).then(function (data) {
                console.log("response data: " + data);
                self.cocktails = data;
            });

            /*fetch next batch of cocktails when user paginates*/
            self.getNextBatchOfCocktails = function (newPageNumber, oldPageNumber) {
                var offset = self.showPages * oldPageNumber;
                var limit = self.showPages;

                restService.getCocktails(offset, limit).then(function (data) {
                    console.log("response data: " + data);
                    self.cocktails = data;
                });
            };

            /*change number of displayed cocktails*/
            self.changeDisplayNumber = function (number) {
                this.showPages = number;
                restService.getCocktails(0, number).then(function (data) {
                    console.log("response data: " + data);
                    self.cocktails = data;
                });

            };

        });
})();