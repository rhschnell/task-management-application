package client.windows.workspace.lock;

import commons.Board;

public class LockPopUpService {


    public boolean verifyPassword(Board board, String text) {
        return board.verifyPassword(text);
    }
}
