package client.utils;

import commons.Board;
import commons.Route;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class BoardUtils {
    private ServerUtils serverUtils;

    /**
     * Creates a new BoardUtils object
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public BoardUtils(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    /**
     * Sends a post request to the server to add a board to the database
     * @param board The board to add
     * @return The added board
     */
    public Board addBoard(Board board)
    {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.BOARD)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Sends a request to the server to get all boards from the database
     * @return List of all boards in the database
     */
    public List<Board> getBoards()
    {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverUtils.getServer()).path(Route.BOARD) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }
}
