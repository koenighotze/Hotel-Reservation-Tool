"use strict";

(function () {
    var tabs = new Map(
        [
            ["div#guest", "http://localhost:8080/guest/guestbook.html"],
            ["div#facilities", "http://localhost:8080/facilities/rooms.html"],
            ["div#booking", "http://localhost:8080/booking/bookings.html"]
        ]
    );

    var contentMarker = "div.starter-template";

    function loadSubPanels() {
        function loadPanel(val, key, map) {
            console.log("Loading data from " + val + " for tab " + key);
            var $tab = $(key);
            $tab.load(val + " " + contentMarker, function (res, sts) {
                if ("error" == sts) {
                    $tab.html("Data is currently unavailable");
                }
            });
        }

        tabs.forEach(loadPanel);
    }

    $(document).ready(function () {
        $("a.sublink").each(function (i, e) {
            var $elem = $(e);

            $.ajax({
                type: 'HEAD',
                url: e.href,
                success: function() {
                    var container = $("div.dynamic-content");
                    $elem.click(function (event) {
                        console.log("Loading data from " + event.target.href);
                        container.load(event.target.href +  " div.starter-template");
                        $("li.active").removeClass("active");
                        $elem.closest("li").addClass("active");
                        event.preventDefault();
                    });
                    return false;
                },
                error: function() {
                    $($elem.closest("li")).addClass("disabled");
                    $elem.click(function (event) {
                        event.preventDefault();
                    });
                }
            });
        });
    });
})();