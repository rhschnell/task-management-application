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

    /**
     * The serverUtils in order to communicate
     * @param serverUtils the serverUtils
     */
    @Inject
    public WebsocketUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    /**
     * Setter for url
     * @param url new url
     */
    public void setUrl(String url) {
        WebsocketUtils.url = "ws://" + url + "/websocket";
        session = connect(WebsocketUtils.url);
    }

    private static String url;
    private static StompSession session;

    /**
     * Connects the subscriber to the url
     * @param url the url to connect to
     * @return session of stomp
     */
    private StompSession connect(String url) {
        var client = new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            System.out.println(url);
            return client.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Register for the updates
     * @param dest
     * @param type
     * @param consumer
     * @return the return object
     * @param <T>
     */
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
}
