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
                var lang = document.getElementById("navbarDropdown").textContent;

                console.log("chosen language: " + lang);

                /*calls are asynchronous so .get() and .then() return promises */
                var promise1 = $http.get('http://localhost:8080/webapi/catalog/form/data/' + lang);
                var promise2 = promise1.then(function (response) {
                        return response.data;
                    });
                return promise2;
            }
            
            self.getCocktails = function (offset, limit) {
                var lang = document.getElementById("navbarDropdown").textContent;
                var promise1 = $http.get('http://localhost:8080/webapi/cocktails/' + lang +
                    '/' + offset + '/' + limit);
                var promise2 = promise1.then(function (response) {
                    return response.data;
                });
                return promise2;
            }

        });
})();