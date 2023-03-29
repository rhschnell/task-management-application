package client.windows.lists.list;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class ListService {
    private final CardListUtils listServer;
    private final CardUtils cardServer;

    @Inject
    public ListService(CardListUtils listServer, CardUtils cardServer) {
        this.listServer = listServer;
        this.cardServer = cardServer;
    }



    public void deleteFromCardList(Card card)
    {
        cardServer.deleteFromCardList(card);
    }
    public CardList getCardList(long id)
    {
        return listServer.getCardList(id);
    }
    public void insertCardList(CardList cardList) {
        listServer.insertCardList(cardList);
    }
}
