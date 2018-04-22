(function () {
    angular.module("search", [])
        .controller("SearchCtrl", function ($window) {
            var self = this;

            self.showPopUp = false;
            self.searchText = '';
            self.highlighted = false;

            self.showPop = function () {
                self.showPopUp = true;
            }

            self.hidePop = function () {
                var property = window.getComputedStyle(document.querySelector('#pop')).zIndex

                console.log("color: " + property);

                if(property != 15){
                    self.showPopUp = false;
                }
            }

            //esc: close search pop-up
            self.hidePopUpKey = function (event) {
                if (event.keyCode == 27) {
                    self.showPopUp = false;
                    document.getElementById("searchInput").blur();
                }
            }

            self.changeBckd = function (paragraphId) {
                if (paragraphId == 'fixedItem'){
                    if (!self.highlighted) {
                        document.getElementById(paragraphId).classList.add('showAllActive');
                        self.highlighted = true;
                    } else {
                        document.getElementById(paragraphId).classList.remove('showAllActive');
                        self.highlighted = false;
                    }
                }else{
                    if (!self.highlighted) {
                        document.getElementById(paragraphId).classList.add('searchItemsActive');
                        self.highlighted = true;
                    } else {
                        document.getElementById(paragraphId).classList.remove('searchItemsActive');
                        self.highlighted = false;
                    }
                }
            }

            self.changePop = function (val) {
                if(val == 'add'){
                    document.getElementById("pop").classList.add('searchPopActive');
                }else{
                    document.getElementById("pop").classList.remove('searchPopActive');
                }

            }

            self.initF = function () {
                document.getElementById('pop').style.visibility = 'visible';
            }

            self.browseCocktail = function (cocktailId) {
                self.showPopUp = false;
                document.getElementById("searchInput").blur();
                $window.location.href = '/controller?command=cocktail&id=' + cocktailId;
            }

            self.browseAll = function () {
                self.showPopUp = false;
                document.getElementById("searchInput").blur();
                $window.location.href = '/controller?command=search';
            }
        })
})();