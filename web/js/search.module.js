(function () {
    angular.module("search", [])
        .controller("SearchCtrl", function (restService) {
            var self = this;

            self.showPopUp = false;
            self.searchText = '';
            self.highlighted = false;

            self.showHidePop = function () {
                self.showPopUp ? self.showPopUp = false : self.showPopUp = true;
            }

            //esc: close search pop-up
            self.hidePopUpKey = function (event) {
                if (event.keyCode == 27) {
                    self.showHidePop();
                    document.getElementById("searchInput").blur();
                }
            }

            self.changeBckd = function (id) {
                console.log("got id: " + id);

                if (id == 'fixedItem'){
                    if (!self.highlighted) {
                        document.getElementById(id).classList.add('showAllActive');
                        self.highlighted = true;
                    } else {
                        document.getElementById(id).classList.remove('showAllActive');
                        self.highlighted = false;
                    }
                }else{
                    if (!self.highlighted) {
                        document.getElementById(id).classList.add('searchItemsActive');
                        self.highlighted = true;
                    } else {
                        document.getElementById(id).classList.remove('searchItemsActive');
                        self.highlighted = false;
                    }
                }
            }

            self.initF = function () {
                document.getElementById('pop').style.visibility = 'visible';
            }
        })
})();