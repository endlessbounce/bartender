(function () {
    angular.module("catalog")
        .controller("UserCocktailCtrl", function (restService, $window) {
            var self = this;
            self.userID = null;
            self.showButtons = false;

            self.init = function (userID) {
                self.userID = userID;
                console.log("UserCocktailCtrl userID " + userID);
                self.showButtons = true;
            }

            self.deleteCreatedC = function (cocktailID) {
                console.log("deleting: " + cocktailID + " of user : " + self.userID);
                restService.deleteCreatedCocktail(cocktailID, self.userID)
                    .then(function (data) {
                        console.log("successful deletion: " + data);
                        $window.location.href = '/controller?command=profile&section=created';
                    });
            }
        });
})();
