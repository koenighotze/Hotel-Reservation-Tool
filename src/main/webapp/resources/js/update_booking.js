(function () {
    "use strict";

    console.log("setting up websocket");
    var ws = new WebSocket("ws://localhost:8080/jee7hotel/booking/tracking");
    
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
        console.log("Connected " + event);
    };

    var onMessage = function(event) {
        updateBooking(event);
    };


    var updateBooking = function(event) {
        console.log("Received " + event);
        var jsonObject = JSON.parse(event.data);

        if (currResNumber === jsonObject.reservationNumber) {
            console.log("new status " + jsonObject.newState);
            statusField.slideUp(500).promise().done(function() {
                statusField.val(jsonObject.newState);
                statusField.slideDown(500);
            });
        }
    };
})();
