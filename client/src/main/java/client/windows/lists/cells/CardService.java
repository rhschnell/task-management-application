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

    public void deleteCard(Card card) {
        cardUtils.deleteCard(card.getId());
    }

    public void insertCard(Card card) {
        cardUtils.insertCard(card);
    }

    public void insertCardList(CardList cardList) {
        cardListUtils.insertCardList(cardList);
    }

}
