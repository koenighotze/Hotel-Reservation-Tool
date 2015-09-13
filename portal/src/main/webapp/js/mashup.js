"use strict";

(function (mashupLinkSelector, mashupContentMarker, mashupContentTarget) {
    $(document).ready(function () {
        $(mashupLinkSelector).each(function (i, e) {
            var $elem = $(e);
            var container = $(mashupContentTarget);

            $.ajax({
                type: 'HEAD',
                url: e.href,
                success: function() {
                    $elem.click(function (event) {
                        console.log("Loading data from " + event.target.href);
                        container.load(event.target.href +  mashupContentMarker);
                        // todo: after load modify links in inner panel
                        $("li.active").removeClass("active");
                        $elem.closest("li").addClass("active");
                        event.preventDefault();
                    });
                    return false;
                },
                error: function() {
                    $($elem.closest("li")).addClass("disabled");
                    $elem.click(function (event) {
                        // todo: add timeout for retry
                        event.preventDefault();
                    });
                }
            });
        });
    });
})("a.mashup", " div.starter-template", "div.dynamic-content");