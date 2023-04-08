package client.windows.lists.cells;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;


public class CardService {
    private final CardListUtils cardListUtils;
    private String boardKey;
    private CardUtils cardUtils;

    /**
     * Inject the servers
     *
     * @param cardUtils Utility class that provides functionality for cards
     * @param cardListUtils Utility class that provides functionality for card lists
     */
    @Inject
    public CardService(CardUtils cardUtils, CardListUtils cardListUtils) {
        this.cardUtils = cardUtils;
        this.cardListUtils = cardListUtils;
    }

    /**
     * Deletes the card
     *
     * @param card , the card that needs to be deleted
     */
    public void deleteCard(Card card) {
        cardUtils.deleteCard(card.getId());
        cardUtils.deleteCardFromDatabase(card.getId());
    }

    /**
     * Inserts a new card into the database
     *
     * @param card     the Card that needs to be inserted
     * @param cardList the card list to update
     */
    public void insertCard(Card card, CardList cardList) {
        card.setPriority(cardList.getCards().size() + 1);
        cardList.addCard(card);
        insertCardList(cardList);
    }

    /**
     * Updates a card by inserting it again into the database
     * @param card the Card that needs to be updated
     */
    public void updateCard(Card card){
        cardUtils.insertCard(card);
    }

    /**
     * Inserts a new CardList into the database
     *
     * @param cardList the CardList that needs to be inserted
     */
    public void insertCardList(CardList cardList) {
        cardListUtils.insertCardList(cardList);
    }

    /**
     * Gets the boardKey
     *
     * @return The board key
     */
    public String getBoardKey() {
        return boardKey;
    }

    /**
     * Sets the boardKey
     *
     * @param boardKey The new board key
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Getter for a card based on its ID
     * @param id the id of the card to be retrieved
     * @return the card with that ID
     */
    public Card getCardByID(long id) {
        return cardUtils.getCardById(id);
    }
}
