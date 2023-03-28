package client.windows.lists.list;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;

public class ListService {
    private final CardListUtils listServer;
    private final CardUtils cardServer;
    private final CardListUtils cardListUtils;

    @Inject
    public ListService(CardListUtils listServer, CardUtils cardServer,CardListUtils cardListUtils) {
        this.listServer = listServer;
        this.cardServer = cardServer;
        this.cardListUtils = cardListUtils;
    }



    public void deleteFromCardList(Card card)
    {
        cardListUtils.deleteFromCardList(card);
    }
    public CardList getCardList(long id)
    {
        return cardListUtils.getCardList(id);
    }
    public void insertCardList(CardList cardList) {
        listServer.insertCardList(cardList);
    }
}
