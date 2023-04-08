package client.windows.lists.delete;

import client.serverUtils.CardListUtils;
import com.google.inject.Inject;

public class DeleteListService {
    private final CardListUtils server;

    /**
     * Constructor for the DeleteListService
     * @param server a CardListUtils instance
     */
    @Inject
    public DeleteListService(CardListUtils server) {
        this.server = server;
    }

    /**
     * Deletes a card list a given id
     * @param id the id of the card list to be deleted
     */
    public void deleteCardList(long id) {
        server.deleteCardList(id);
    }
}
