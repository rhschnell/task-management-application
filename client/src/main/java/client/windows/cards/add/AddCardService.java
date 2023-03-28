package client.windows.cards.add;

import client.serverUtils.CardListUtils;
import com.google.inject.Inject;
import commons.CardList;

public class AddCardService {
    private final CardListUtils server;

    @Inject
    public AddCardService(CardListUtils server) {

        this.server = server;
    }

    public Long getLastId()
    {
        return server.getLastId();
    }
    public void insertCardList(CardList cardList) {
        server.insertCardList(cardList);
    }
}
