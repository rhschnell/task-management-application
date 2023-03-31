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

    @Inject
    public AdminService(BoardUtils server) {
        this.server = server;
    }

    public void insertBoard(Board board) {
        server.insertBoard(board);
    }

    public Board getBoard(String key) {
        return server.getBoard(key);
    }

    public List<Board> getBoards() {
        return server.getBoards();
    }

    public void deleteBoard(Board board) {
        server.deleteBoard(board.getKey());
    }

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
