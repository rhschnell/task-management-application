package client.windows.cards.view;

import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;

public class ViewCardService {

    private final CardUtils server;
    private String boardKey;

    /**
     * Constructor for the ViewCardService
     * @param server a CardUtils instance
     */
    @Inject
    public ViewCardService(CardUtils server) {
        this.server = server;
    }

    /**
     * Getter for the key of the board containing the card
     * @return the key of the board
     */
    public String getBoardKey() {
        return boardKey;
    }

    /**
     * Retrieves a card with a given id from the database if it exists
     * or null otherwise
     * @param id the id of the card to be retrieved
     * @return the card from the server, or null if it does not exist
     */
    public Card getCard(long id) {
        return server.getCardById(id);
    }

    /**
     * Setter for the key of the board
     * @param boardKey the key of the board
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }
}
