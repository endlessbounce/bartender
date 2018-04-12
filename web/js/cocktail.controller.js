(function () {
    angular.module("catalog")
        .controller("CocktailCtrl", function (restService) {
            var self = this;
            self.cocktailID = document.getElementById("cocktailId").textContent;
            self.userID = document.getElementById("userID").textContent;

            console.log("id user: " + self.userID + " cocktail id: " + self.cocktailID);

            //self.cocktailID, self.userID
            restService.liked(self.cocktailID, self.userID).then(function (data) {
                console.log("id of liked cocktail: " + data.id);
                if (data.id != 0) {
                    self.isFavourite = true;
                }

            });

            self.like = function () {
                self.isFavourite = true;
            }

            self.unlike = function () {
                self.isFavourite = false;
            }
        });
})();