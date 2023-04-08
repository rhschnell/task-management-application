package client.serverUtils;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebsocketUtils {
    private ServerUtils serverUtils = new ServerUtils();

    @Inject
    public WebsocketUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }
    private final String url = "ws://" + serverUtils.getServer().substring(7) + "/websocket";
    private final StompSession session = connect(url);
    private StompSession connect(String url) {
        var client = new WebSocketStompClient(new StandardWebSocketClient());
        //var stomp = new WebSocketStompClient(client);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return client.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T>StompSession.Subscription registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        return session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }
    public void send(String dest, Object o) {
        session.send(dest, o);
    }
}
