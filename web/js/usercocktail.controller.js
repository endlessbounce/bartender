(function () {
    angular.module("catalog")
        .controller("UserCocktailCtrl", function (restService, $window) {
            var self = this;
            self.userID = null;

            self.init = function (userID) {
                console.log("UserCocktailCtrl userID " + userID);
                self.userID = userID;
            }

            self.deleteCreatedC = function (cocktailID) {
                console.log("deleting: " + cocktailID);
                restService.deleteCreatedCocktail(cocktailID, self.userID)
                    .then(function () {
                        $window.location.href = '/controller?command=profile&section=created';
                    });
            }
        });
})();
