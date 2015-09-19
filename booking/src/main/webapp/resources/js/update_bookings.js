var setupWs = function (config) {
    "use strict";

    console.log("setting up websocket");
    var ws = new WebSocket(config.webSocketBaseUrl()  + "/booking/tracking");

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
            field
                .fadeOut('slow')
                .promise()
                .done(function() {
                    field.text(jsonObject.newState);
                    field.fadeIn('slow');
                });
        }
    };
};
