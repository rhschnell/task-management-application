package client.windows.lists.delete;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import com.google.inject.Inject;
import commons.Board;

public class DeleteListService {
    private final CardListUtils server;
    private final BoardUtils boardUtils;
    private String boardKey;

    /**
     * Constructor for the List
     * @param server the server
     * @param boardUtils the boardUtils
     */
    @Inject
    public DeleteListService(CardListUtils server, BoardUtils boardUtils) {
        this.server = server;
        this.boardUtils=boardUtils;
    }

    /**
     * Sets the boardKey
     * @param boardKey the boardKey
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Deletes a card list a given id
     * @param id the id of the card list to be deleted
     */
    public void deleteCardList(long id) {
        Board toInsert = boardUtils.getBoard(boardKey);
        toInsert.removeList(server.getCardList(id));
        boardUtils.insertBoard(toInsert);

    }
}
