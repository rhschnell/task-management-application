package client.windows.lists.cells;

import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;

public class RenameCardService {
    private final CardUtils server;

    /**
     * Injectable constructor for the RenameCardService
     * @param server the utility to communicate with the server
     */
    @Inject
    public RenameCardService(CardUtils server) {
        this.server = server;
    }

    /**
     * Insert given card into the database.
     * @param card card to be inserted
     */
    public void insertCard(Card card) {
        server.insertCard(card);
    }

}
