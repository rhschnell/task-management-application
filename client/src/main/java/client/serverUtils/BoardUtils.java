package client.serverUtils;

import commons.Board;
import commons.Route;
import commons.Tag;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class BoardUtils {
    private ServerUtils serverUtils = new ServerUtils();
    private Client client;

    /**
     * Creates a new BoardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public BoardUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
        this.client = ClientBuilder.newClient(new ClientConfig());
    }

    /**
     * Sends a request to the server to delete a certain board from the database
     * @param key of board to delete
     */
    public void deleteBoard(String key) {
        client.target(serverUtils.getServer()).path(Route.BOARD + "/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Response.class);
    }

    /**
     * Sends a request to the server to retrieve a certain board from the database
     * @param key the key of the board to find
     * @return the desired board
     */
    public Board getBoard(String key) {
        return client.target(serverUtils.getServer()).path(Route.BOARD + "/" + key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Board.class);
    }

    /**
     * Sends a request to the server to get all boards from the database
     * @return List of all boards in the database
     */
    public List<Board> getBoards() {
        return client.target(serverUtils.getServer()).path(Route.BOARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {});
    }

    /**
     * Sends a post request to the server to add a board to the database
     *
     * @param board The board to add
     * @return The added board
     */

    public Board insertBoard(Board board) {
        return client.target(serverUtils.getServer()).path(Route.BOARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public void insertNewTag(String key, Tag tag) {
        client
                .target(serverUtils.getServer()).path(Route.BOARD+"/addBoardTag/"+key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Board.class);
    }
    public void removeBoardTag(String key, Tag tag) {
        client
                .target(serverUtils.getServer()).path(Route.BOARD+"/removeBoardTag/"+key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Board.class);
    }

    /**
     * Adds a message using websocket technologies
     * @param board the board to add
     */
    @MessageMapping("/boards")
    @SendTo("/topic/boards/titles")
    public String addMessage(Board board) {
        return insertBoard(board).getTitle();
    }

    private final String url = "ws://" + serverUtils.getServer().substring(7) + "/websocket";
    private final StompSession session = connect(url);
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public void registerForMessages(String dest, Consumer<String> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((String) payload);
            }
        });
    }

    public void send(String dest, Object o) {
        session.send(dest, o);
    }

    public void setServer(String server) {
        serverUtils.setServer(server);
    }
    public String getServer()
    {
        return serverUtils.getServer();
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

}
