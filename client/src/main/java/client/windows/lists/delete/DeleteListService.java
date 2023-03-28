package client.windows.lists.delete;

import client.serverUtils.CardListUtils;
import com.google.inject.Inject;

public class DeleteListService {
    private final CardListUtils server;

    @Inject
    public DeleteListService(CardListUtils server) {
        this.server = server;
    }


    public void deleteCardList(long id) {
        server.deleteCardList(id);
    }
}
