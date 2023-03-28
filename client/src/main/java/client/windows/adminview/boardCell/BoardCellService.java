package client.windows.adminview.boardCell;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class BoardCellService {
    private final BoardUtils server;

    @Inject
    public BoardCellService(BoardUtils server) {
        this.server = server;
    }

    public void deleteBoard(Board board) {
        server.deleteBoard(board.getKey());
    }
}
