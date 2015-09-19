"use strict";

(function (mashupLinkSelector, mashupContentMarker, mashupContentTarget) {
  $(document).ready(function () {
    var iframe = $("iframe#frameDemo");
    iframe.load(function () {
      // remove nav bar
      var contents = iframe.contents();

      contents.find("div.navbar-fixed-top").hide();
      contents.find("footer").hide();
      contents.find("body").css({ 'padding-top': "0" });
      contents.find("div.starter-template").css({ 'padding-top': "0" });
    });

    $(mashupLinkSelector).each(function (i, e) {
      var $elem = $(e);
      var container = $(mashupContentTarget);

      $.ajax({
        type: 'HEAD',
        url: e.href,
        success: function () {
          $elem.click(function (event) {

            console.log("Loading data from " + event.target.href);

            $("iframe#frameDemo").attr('src', event.target.href);
            //container.load(event.target.href + mashupContentMarker);

            // todo: after load modify links in inner panel
            $("li.active").removeClass("active");
            $elem.closest("li").addClass("active");
            event.preventDefault();
          });
          return false;
        },
        error: function () {
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