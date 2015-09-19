var setupWs = function (config) {
    "use strict";
    var url = config.webSocketBaseUrl() + "/booking/tracking";
    console.log("setting up websocket on url " + url);
    var ws = new WebSocket(url);

    // the number we display
    var currResNumber = $("#bookingDetailsForm\\:reservation_number").val();
    var statusField = $('#bookingDetailsForm\\:reservationStatus');
    
    ws.onopen = function(event) {
        onConnect(event);
    };
    
    ws.onmessage = function(event) {
        onMessage(event);
    };

    var onConnect = function(event) {
        console.log("Connected ", event);
    };

    var onMessage = function(event) {
        updateBooking(event);
    };


    var updateBooking = function(event) {
        var jsonObject = JSON.parse(event.data);

        if (currResNumber === jsonObject.reservationNumber) {
            var orgWidth = statusField.css('border-width');
            statusField
                .parent()
                    .toggleClass('has-success');
            statusField
                .animate({'borderWidth': 3}, 1000)
                .promise()
                .done(function() {
                    statusField.val(jsonObject.newState);
                    statusField
                        .animate({'borderWidth' : orgWidth }, 1000)
                        .promise()
                        .done(function() { statusField.parent().toggleClass('has-success'); });
                });
        }
    };
};
