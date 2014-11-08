(function () {
    console.log("setting up websocket");
    var ws = new WebSocket("ws://localhost:8080/jee7quickstart/booking/tracking");
    
    // the number we display
    var currResNumber = $("#bookingDetailsForm\\:reservation_number").val();
    var statusField = $('#bookingDetailsForm\\:reservationStatus');
    
    ws.onopen = function(event) {
        onConnect(event);
    };
    
    ws.onmessage = function(event) {
        onMessage(event);
    };

    onConnect = function(event) {
        console.log("Connected " + event);
    };

    onMessage = function(event) {
        updateBooking(event);
    };

    updateBooking = function(event) {
        console.log("Received " + event);
        var jsonObject = JSON.parse(event.data);

        if (currResNumber === jsonObject.reservationNumber) {
            console.log("shoud look for status and update to " + jsonObject.reservationStatus);
            console.log("new status " + jsonObject.newState);

            statusField.val(jsonObject.newState);
        }
    };
})();
