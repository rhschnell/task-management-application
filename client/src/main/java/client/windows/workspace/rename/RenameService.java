package client.windows.workspace.rename;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class RenameService {
    private final BoardUtils server;

    @Inject
    public RenameService(BoardUtils server) {
        this.server = server;
    }

    public void insertBoard(Board board) {
        server.insertBoard(board);
    }
}
