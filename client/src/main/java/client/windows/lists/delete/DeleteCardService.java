package client.windows.lists.delete;

import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;

public class DeleteCardService {
    private final CardUtils server;

    /**
     * Injects the server
     * @param server the cardutils
     */
    @Inject
    public DeleteCardService(CardUtils server) {
        this.server = server;
    }


    /**
     * Deletes the card
     * @param card , the card that needs to be deleted
     */

    public void deleteCard(Card card) {
        server.deleteCard(card.getId());
        server.deleteCardFromDatabase(card.getId());

    }
}
