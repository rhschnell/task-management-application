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

import javax.inject.Inject;
import java.util.List;

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

    public void setServerUtils(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
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

    /**
     * This method inserts a new tag to a given board
     * @param key the key of the board
     * @param tag the tag to be inserted
     */
    public void insertNewTag(String key, Tag tag) {
        client
                .target(serverUtils.getServer()).path(Route.BOARD+"/addBoardTag/"+key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Board.class);
    }

    /**
     * This method removes a tag from a board
     * @param key the key of the board
     * @param tag the tag to be removed
     */
    public void removeBoardTag(String key, Tag tag) {
        client
                .target(serverUtils.getServer()).path(Route.BOARD+"/removeBoardTag/"+key)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tag, APPLICATION_JSON), Board.class);
    }

    /**
     * Setter for the server address
     * @param server the server address
     */
    public void setServer(String server) {
        serverUtils.setServer(server);
    }

    /**
     * Getter for the server address
     * @return the server address
     */
    public String getServer()
    {
        return serverUtils.getServer();
    }

    /**
     * Setter for the client
     * @param client the client
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

}
