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
     * Deletes the card from the server
     * @param card
     */
    public void deleteCard(Card card)
    {
        server.deleteFromCardList(card);
        server.deleteCard(card.getId());
    }

    /**
     * Getter for the key of the board containing the card
     * @return the key of the board
     */
    public String getBoardKey() {
        return boardKey;
    }

    /**
     * Setter for the key of the board
     * @param boardKey the key of the board
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }
}
