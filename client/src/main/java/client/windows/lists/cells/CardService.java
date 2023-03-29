package client.windows.lists.cells;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class CardService {
    private final CardUtils cardUtils;
    private final CardListUtils cardListUtils;

    @Inject
    public CardService(CardUtils cardUtils, CardListUtils cardListUtils) {
        this.cardUtils = cardUtils;
        this.cardListUtils = cardListUtils;
    }

    /**
     * Deletes the card
     * @param card , the card that needs to be deleted
     */
    public void deleteCard(Card card) {
        cardUtils.deleteFromCardList(card);
        cardUtils.deleteCard(card.getId());
    }

    /**
     * Inserts a card into the database
     * @param card the Card that needs to be inserted
     */
    public void insertCard(Card card) {
        cardUtils.insertCard(card);
    }

    /**
     * Inserts a new CardList into the database
     * @param cardList the CardList that needs to be inserted
     */
    public void insertCardList(CardList cardList) {
        cardListUtils.insertCardList(cardList);
    }

}
