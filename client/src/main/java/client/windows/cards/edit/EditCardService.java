package client.windows.cards.edit;

import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;

public class EditCardService {
    private final CardUtils server;

    @Inject
    public EditCardService(CardUtils server) {
        this.server = server;
    }

    public void insertCard(Card card) {
        server.insertCard(card);
    }
}
