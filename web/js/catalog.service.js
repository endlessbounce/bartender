/*IIFE - immediately invoked function expression
* to isolate vars from global name space, and restrict the scope
* to this file only*/
(function () {
    angular.module("catalog")
        .service("restService", function ($http) {
            var self = this;

            /*make request inside the function*/
            self.getFormData = function () {
                /*get chosen language*/
                var lang = document.getElementById("navbarDropdown").textContent.trim();

                console.log("chosen language: " + lang);

                /*calls are asynchronous so .get() and .then() return promises */
                var promise1 = $http.get('http://localhost:8080/webapi/catalog/form/data/' + lang);
                var promise2 = promise1.then(function (response) {
                        return response.data;
                    });
                return promise2;
            };

            self.getCocktails = function () {
                var lang = document.getElementById("navbarDropdown").textContent.trim();
                var promise1 = $http.get('http://localhost:8080/webapi/cocktails?locale=' + lang);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                });
                return promise2;
            };

            self.getCocktailsWithParams = function (url) {
                var lang = document.getElementById("navbarDropdown").textContent.trim();
                var promise1 = $http.get(url + "&locale=" + lang);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                });
                return promise2;
            };

            /*check if the cocktail is in the list of favourite user's cocktails*/
            self.isCocktailliked = function (cocktailID, userID) {
                var promise1 = $http.get('http://localhost:8080/webapi/user/' + userID + '/favourite/' + cocktailID);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

            self.addLiked = function (cocktailID, userID) {
                var promise1 = $http.post('http://localhost:8080/webapi/user/' + userID + '/favourite', {"id": cocktailID});
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

            self.addCreated = function (cocktail, userID) {
                var lang = document.getElementById("navbarDropdown").textContent.trim();
                var promise1 = $http.post('http://localhost:8080/webapi/user/' + userID + '/created?locale=' + lang, cocktail);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    throw response.data;
                });
                return promise2;
            };

            self.deleteLiked = function (cocktailID, userID) {
                var promise1 = $http.delete('http://localhost:8080/webapi/user/' + userID + '/favourite/' + cocktailID);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

            self.deleteCreatedCocktail = function (cocktailID, userID) {
                var path = 'http://localhost:8080/webapi/user/' + userID + '/created/' + cocktailID;
                console.log("delete request: " + path);
                var promise1 = $http.delete(path);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

            self.getAllFavourite = function (userID) {
                var lang = document.getElementById("navbarDropdown").textContent.trim();
                var path = 'http://localhost:8080/webapi/user/' + userID + '/favourite' + "?locale=" + lang;

                console.log(path);

                var promise1 = $http.get(path);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

            self.getAllCreated = function (userID) {
                var lang = document.getElementById("navbarDropdown").textContent.trim();
                var path = 'http://localhost:8080/webapi/user/' + userID + '/created' + "?locale=" + lang;

                console.log(path);

                var promise1 = $http.get(path);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                }, function (response) {
                    console.log(response.data)
                });
                return promise2;
            };

        });
})();