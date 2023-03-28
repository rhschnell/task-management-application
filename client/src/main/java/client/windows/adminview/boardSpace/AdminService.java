package client.windows.adminview.boardSpace;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

import java.util.List;

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
}
