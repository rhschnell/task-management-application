package client.windows.customize.cards.add;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class AddCardPresetService {
    private final BoardUtils server;

    /**
     * Constructor for the AddCardPresetService
     * @param server the BoardUtils server
     */
    @Inject
    AddCardPresetService(BoardUtils server){
        this.server = server;
    }

    /**
     * Inserts the board to the server, or updates it
     * @param board the board to be inserted or update
     */
    public void insertBoard(Board board){
        server.insertBoard(board);
    }
}