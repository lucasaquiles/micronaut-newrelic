var hash = document.location.hash.split("/");

var ws = new WebSocket('ws://'+location.hostname+':'+location.port+'/ws/chat/'+hash[1]+'/'+hash[2])

ws.onmessage = function (msg) {
    updateMessage(msg.data);
};

ws.onclose = function () { alert("WebSocket connection closed") };


function updateMessage(msg) {
    jQuery('#chat ul').append("<li>"+msg+"</li>");
}

jQuery(document).ready(function() {

    jQuery("button#send-message").click(function(e){
        var msg = jQuery("textarea#message-area").val();
        console.log("opa", msg);
        ws.send(msg);
    });
})

