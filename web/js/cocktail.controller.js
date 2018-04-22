(function () {
    angular.module("catalog")
        .controller("CocktailCtrl", function (restService) {
            var self = this;
            self.cocktailID = document.getElementById("cocktailId").textContent;
            self.userID = document.getElementById("userID").textContent;

            console.log("id user: " + self.userID + " cocktail id: " + self.cocktailID);

            //self.cocktailID, self.userID
            restService.isCocktailliked(self.cocktailID, self.userID).then(function (data) {
                console.log("id of liked cocktail: " + data.id);
                if (data.id != 0) {
                    self.isFavourite = true;
                }

                var likeButtons = document.getElementById("likeButtons");

                if(likeButtons){
                    likeButtons.style.visibility = "visible";
                }

            });

            self.like = function () {
                restService.addLiked(self.cocktailID, self.userID);
                self.isFavourite = true;
            }

            self.unlike = function () {
                restService.deleteLiked(self.cocktailID, self.userID);
                self.isFavourite = false;
            }
        });
})();