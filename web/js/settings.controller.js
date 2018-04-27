(function () {
    angular.module("catalog")
        .controller("SettingsCtrl", function (restService, $window) {
            var self = this;
            self.userID = null;
            self.showContent = false;

            self.user = {
                id: '',
                name: '',
                email: '',
                hashKey: '',
                salt: '',
                registrationDate: '',
                uniqueCookie: ''
            }

            self.oldPassword = '';
            self.password = '';
            self.passwordConfirm = '';
            self.passwordsDontMatch = false;

            self.operationFailure = false;
            self.operationSuccess = false;

            self.init = function (userID) {
                self.userID = userID;
                self.user.id = userID;
                console.log("SettingsCtrl userID " + userID);
                self.showContent = true;
                document.getElementById("operationFailure1").style.display = 'inline';
            }

            self.clean = function () {
                self.operationFailure = false;
                self.operationSuccess = false;
                self.passwordsDontMatch = false;
            }

            self.deleteProfile = function () {
                restService.deleteUser(self.userID).then(
                    function () {
                        console.log('user deleted: ' + self.userID);
                        $window.location.href = '/controller?command=logout';
                    },
                    function () {
                        self.operationFailure = true;
                    }
                );
            }

            self.updateProfile = function (parameter) {
                self.clean();

                var pswdPart = '';

                if(parameter == 'password' && self.passwordsDontMatch == true){
                    return;
                }else if (parameter == 'password' && self.passwordsDontMatch == false){
                    pswdPart = '&password=' + self.oldPassword + '&newpassword=' + self.password;
                }

                restService.updateUser(self.user, parameter, pswdPart).then(
                    function () {
                        /* to update the name in the navBar reload the page */
                        if (parameter == 'name') {
                            $window.location.href = '/controller?command=settings';
                        }

                        self.operationSuccess = true;

                    },
                    function () {
                        self.operationFailure = true;
                    }
                );

            }

            self.checkPasswords = function () {

                if(self.password == self.passwordConfirm){
                    self.passwordsDontMatch = false;
                    document.getElementById("pswd").style.borderColor = "#ced4da";
                    document.getElementById("newPswd").style.borderColor = "#ced4da";
                } else {
                    self.passwordsDontMatch = true;
                    document.getElementById("pswd").style.borderColor = "red";
                    document.getElementById("newPswd").style.borderColor = "red";
                }
            }
        });
})();
