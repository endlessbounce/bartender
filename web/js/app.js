/*initialize module with import modules*/
var app = angular.module("catalog", [
    'angularUtils.directives.dirPagination',
    'search'
]);

/*F5 protection*/
document.onkeydown = function (event) {
    switch (event.keyCode) {
        case 116 : //F5 button
            event.returnValue = false;
            event.keyCode = 0;
            return false;
        case 82 : //R button
            if (event.ctrlKey) {
                event.returnValue = false;
                event.keyCode = 0;
                return false;
            }
    }
};


