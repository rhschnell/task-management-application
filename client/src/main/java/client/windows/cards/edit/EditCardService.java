package client.windows.cards.edit;

import client.serverUtils.CardUtils;
import commons.Card;

public class EditCardService {
    private final CardUtils server;

    public EditCardService(CardUtils server) {
        this.server = server;
    }

    public void insertCard(Card card) {
        server.insertCard(card);
    }
}
