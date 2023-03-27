package client.windows.lists.list;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import commons.CardList;

public class ListService {
    private final CardListUtils listServer;
    private final CardUtils cardServer;

    public ListService(CardListUtils listServer, CardUtils cardServer) {
        this.listServer = listServer;
        this.cardServer = cardServer;
    }


    public void deleteCard(long id) {
        cardServer.deleteCard(id);
    }

    public void insertCardList(CardList cardList) {
        listServer.insertCardList(cardList);
    }
}
