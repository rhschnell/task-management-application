package client.windows.workspace.boardSpace;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.scene.input.Clipboard;

public class WorkspaceService {
    private final BoardUtils server;

    @Inject
    public WorkspaceService(BoardUtils server) {
        this.server = server;
    }

    public void insertBoard(Board board) {
        server.insertBoard(board);
    }

    public Board getBoard(String key) {
        return server.getBoard(key);
    }

    public void deleteBoard(Board board) {
        server.deleteBoard(board.getKey());
    }

    public void copyKey(String key) {
        Clipboard clipboard = Clipboard.getSystemClipBoard();
    }
}
