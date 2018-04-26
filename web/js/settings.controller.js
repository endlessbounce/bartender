(function () {
    angular.module("catalog")
        .controller("SettingsCtrl", function (restService, $window) {
            var self = this;
            self.userID = null;
            self.showContent = false;

            self.operationFailure = false;

            self.init = function (userID) {
                self.userID = userID;
                console.log("SettingsCtrl userID " + userID);
                self.showContent = true;
            }

            self.clean = function () {
                self.operationFailure = false;
            }

            self.deleteProfile = function () {
                restService.deleteUser(self.userID).then(
                    function () {
                        console.log('user deleted: ' + self.userID);
                    },
                    function () {
                        self.operationFailure = true;
                    }
                );

                $window.location.href = '/controller?command=logout';
            }
        });
})();
