package client.windows.adminview.boardSpace;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {
    private final BoardUtils server;

    /**
     * Creates a new AdminService instance
     * @param server Instance of the utility class that handles boards
     */
    @Inject
    public AdminService(BoardUtils server) {
        this.server = server;
    }

    /**
     * Inserts a board into the database
     * @param board The board to insert
     */
    public void insertBoard(Board board) {
        server.insertBoard(board);
    }

    /**
     * Gets a board from the database
     * @param key The key of the board to get from the database
     * @return The requested board
     */
    public Board getBoard(String key) {
        return server.getBoard(key);
    }

    /**
     * Gets all boards from the database
     * @return List of boards that are in the database
     */
    public List<Board> getBoards() {
        return server.getBoards();
    }

    /**
     * Deletes a board from the database
     * @param board The board to delete
     */
    public void deleteBoard(Board board) {
        server.deleteBoard(board.getKey());
    }


    /**
     * Sets the IP address of the server to communicate with
     * @param ip The IP of the server
     */
    public void setServer(String ip) {
        server.setServer(ip);
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
