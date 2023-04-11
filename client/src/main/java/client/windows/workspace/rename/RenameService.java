package client.windows.workspace.rename;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class RenameService {
    private final BoardUtils server;

    /**
     * Injectable constructor for the RenameService
     *
     * @param server the utility to communicate with the server
     */
    @Inject
    public RenameService(BoardUtils server) {
        this.server = server;
    }

    /**
     * Insert given board into the database.
     *
     * @param board Board to be inserted
     */
    public void insertBoard(Board board) {
        server.insertBoard(board);
    }
}
