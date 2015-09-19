/**
 * Created by dschmitz on 21.11.14.
 */

(function(window) {
  "use strict";

  var config =  {

    settings : {
      href: window.location.href,
      hostname: window.location.hostname,
      wsPort: "${ws.port}"
    },

    webSocketBaseUrl : function() {
      return "ws://" + this.settings.hostname + ":" + this.settings.wsPort + this.settings.requestContextPath;
    },

    get : function(key) {
      return this.settings.key;
    },

    setRequestContextPath : function(path) {
      this.settings.requestContextPath = path;
    }
  };

  window.jee7hotelconfig = config;
})(window);


