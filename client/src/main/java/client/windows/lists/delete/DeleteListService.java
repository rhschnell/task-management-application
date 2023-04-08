package client.windows.lists.delete;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import com.google.inject.Inject;
import commons.Board;

public class DeleteListService {
    private final CardListUtils server;
    private final BoardUtils boardUtils;
    private String boardKey;

    @Inject
    public DeleteListService(CardListUtils server, BoardUtils boardUtils) {
        this.server = server;
        this.boardUtils=boardUtils;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    public void deleteCardList(long id) {
        //server.deleteCardList(id);
        Board toInsert = boardUtils.getBoard(boardKey);
        toInsert.removeList(server.getCardList(id));
        boardUtils.insertBoard(toInsert);

    }
}
