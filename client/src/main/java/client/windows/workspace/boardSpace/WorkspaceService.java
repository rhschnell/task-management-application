package client.windows.workspace.boardSpace;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.HashMap;
import java.util.Map;

public class WorkspaceService {
    private final BoardUtils server;

    /**
     * Constructor for the WorkspaceService
     * @param server the BoardUtils server
     */
    @Inject
    public WorkspaceService(BoardUtils server) {
        this.server = server;
    }

    /**
     * Inserts the board to the server, or updates it
     * @param board the board to be inserted or update
     */
    public void insertBoard(Board board) {
        server.insertBoard(board);
    }

    /**
     * Gets the board with the specified key from the server
     * @param key the key for the board to retrieve
     * @return the board that has the specified key
     */
    public Board getBoard(String key) {
        return server.getBoard(key);
    }

    /**
     *Deletes the board from the server
     * @param board the board to be deleted
     */
    public void deleteBoard(Board board) {
        server.deleteBoard(board.getKey());
    }

    /**
     * Copies the entered string to the system clipboard
     * @param key String to copy
     */
    public void copyKey(String key) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Map<DataFormat, Object> clipMap = new HashMap<DataFormat, Object>();
        clipMap.put(DataFormat.PLAIN_TEXT, key);
        clipboard.setContent(clipMap);
    }
}
