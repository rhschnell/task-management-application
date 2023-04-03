package client.windows.workspace.lock;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class LockPopUpService {
    private final BoardUtils server;

    private Board board;

    @Inject
    public LockPopUpService(BoardUtils server) {
        this.server = server;
    }

    public boolean verifyPassword(Board board, String text) {
        return board.verifyPassword(text);
    }



    // SETTERS AND GETTERS

    /**
     * Setter for this controller's board
     * @param board board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for protected status, calls setter in board class
     * @param b whether the board is protected or not
     */
    public void setProtected(boolean b) {
        board.setProtected(b);
        updateDB(board);
    }

    /**
     * Setter for password, calls setter in board class
     * @param pwd the new password to be set
     */
    public void setPassword(String pwd) {
        board.setPassword(pwd);
        updateDB(board);
    }

    /**
     * Makes sure to update the database with new information
     * @param board containing new information
     */
    private void updateDB(Board board) {
        server.insertBoard(board);
    }
}
