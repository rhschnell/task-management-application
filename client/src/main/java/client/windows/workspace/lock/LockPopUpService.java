package client.windows.workspace.lock;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class LockPopUpService {
    private final BoardUtils server;

    private Board board;

    /**
     * Constructor for the LockPopUpService
     *
     * @param server a BoardUtils instance
     */
    @Inject
    public LockPopUpService(BoardUtils server) {
        this.server = server;
    }

    /**
     * Setter for this controller's board
     *
     * @param board board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Setter for protected status, calls setter in board class
     *
     * @param b whether the board is protected or not
     */
    public void setProtected(boolean b) {
        board.setProtected(b);
        updateDB(board);
    }

    /**
     * Setter for password, calls setter in board class
     *
     * @param pwd the new password to be set
     */
    public void setPassword(String pwd) {
        board.setPassword(pwd);
        updateDB(board);
    }

    /**
     * A utility method to verify the password of the board against passed text
     *
     * @param board the board used for the verification
     * @param text  the text to be checked
     * @return a boolean that is true if the password is correct and false otherwise
     */
    public boolean verifyPassword(Board board, String text) {
        return board.verifyPassword(text);
    }

    /**
     * Makes sure to update the database with new information
     *
     * @param board containing new information
     */
    private void updateDB(Board board) {
        server.insertBoard(board);
    }
}
