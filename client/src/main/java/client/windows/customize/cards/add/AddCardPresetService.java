package client.windows.customize.cards.add;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Board;

public class AddCardPresetService {
    private final BoardUtils server;

    @Inject
    AddCardPresetService(BoardUtils server){
        this.server = server;
    }

    public void insertBoard(Board board){
        server.insertBoard(board);
    }
}