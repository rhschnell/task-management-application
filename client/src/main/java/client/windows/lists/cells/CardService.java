package client.windows.lists.cells;

import client.serverUtils.CardUtils;
import commons.Card;

public class CardService {
    private final CardUtils cardUtils;

    public CardService(CardUtils cardUtils) {
        this.cardUtils = cardUtils;
    }

    public void deleteCard(Card card) {
        cardUtils.deleteCard(card.getId());
    }
}
