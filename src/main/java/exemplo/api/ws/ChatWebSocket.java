package exemplo.api.ws;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;

@ServerWebSocket("/ws/chat/{topic}/{username}")
public class ChatWebSocket {

    private WebSocketBroadcaster broadcaster;

    public ChatWebSocket(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @Trace(metricName = "ChatWebSocket.onOpen", dispatcher = true)
    @OnOpen
    public Publisher<String> onOpen(final String username, final String topic, final WebSocketSession session) {
        final String msg = "" + username + " entrou na sala";

        HashMap<String, Object> map = new HashMap<>();
        map.put("usuario", username);
        map.put("hora", new Date());

        NewRelic.getAgent().getInsights().recordCustomEvent("CONEXOES_OPEN", map);

        return broadcaster.broadcast(msg, isValid(topic));
    }

    @Trace(metricName = "ChatWebSocket.onMessage", dispatcher = true)
    @OnMessage
    public Publisher<String> onMessage(final String topic, final String username, final String message, final WebSocketSession session) {
        final String msg = "[" + username + "] " + message;


        HashMap<String, Object> map = new HashMap<>();
        map.put("usuario", username);
        map.put("message", message.hashCode());

        NewRelic.getAgent().getInsights().recordCustomEvent("MESSAGES", map);

        return broadcaster.broadcast(msg, isValid(topic));
    }

    @Trace(metricName = "ChatWebSocket.onClose", dispatcher = true)
    @OnClose
    public Publisher<String> onClose( String topic,  String username, WebSocketSession session) {
        String msg = "[" + username + "] Disconnected!";
        return broadcaster.broadcast(msg, isValid(topic));
    }


    private Predicate<WebSocketSession> isValid(final String topic) {
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
