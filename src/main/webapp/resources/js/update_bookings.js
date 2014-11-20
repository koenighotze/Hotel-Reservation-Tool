var setupWs = function (contextRoot) {
    "use strict";

    var host = window.location.host;
    console.log("setting up websocket");
    var ws = new WebSocket("ws://" + host + contextRoot + "/booking/tracking");

    ws.onopen = function(event) {
        onConnect(event);
    };
    
    ws.onmessage = function(event) {
        onMessage(event);
    };

    var onConnect = function(event) {
        console.log("Connected " + event);
    };

    var onMessage = function(event) {
        updateBooking(event);
    };

    var updateBooking = function(event) {
        var jsonObject = JSON.parse(event.data);

        var field = $('span#status_' + jsonObject.reservationNumber);
        if (field) {
            field.slideUp(500).promise().done(function() {
                field.text(jsonObject.newState);
                field.slideDown(500);
            });

        }
    };
};
