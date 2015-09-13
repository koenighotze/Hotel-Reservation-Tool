"use strict";

(function (mashupLinkSelector, mashupContentMarker, mashupContentTarget) {
    var tabs = new Map(
        [
            ["div#guest", "http://localhost:8080/guest/guestbook.html"],
            ["div#facilities", "http://localhost:8080/facilities/rooms.html"],
            ["div#booking", "http://localhost:8080/booking/bookings.html"]
        ]
    );

    function initPanels() {
        function loadPanel(val, key, map) {
            console.log("Loading data from " + val + " for tab " + key);
            var $tab = $(key);
            $tab.load(val + " " + mashupContentMarker, function (res, sts) {
                if ("error" == sts) {
                    $tab.html("Data is currently unavailable");
                }
            });
        }

        tabs.forEach(loadPanel);
    }
})("a.mashup", " div.starter-template", "div.dynamic-content");