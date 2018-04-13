(function () {
    angular.module("catalog")
        .controller("ProfileCtrl", function (restService) {
            var self = this;
            self.userID = document.getElementById("userID").textContent;

            //button group show pages
            self.showPages = 12;

            //list of cocktails
            self.cocktails = [];

            console.log("profile controller user id: " + self.userID);

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
        });
})();