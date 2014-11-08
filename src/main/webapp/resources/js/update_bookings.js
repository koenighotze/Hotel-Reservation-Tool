(function () {
    console.log("setting up websocket");
    var ws = new WebSocket("ws://localhost:8080/jee7quickstart/booking/tracking");
    
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
        var jsonObject = JSON.parse(event.data);

        var field = $('span#status_' + jsonObject.reservationNumber)
        if (field) {
            field.text(jsonObject.newState);
        }
    };
})();
