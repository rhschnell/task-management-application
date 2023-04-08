package client.windows.cards.view;

import client.serverUtils.CardUtils;
import com.google.inject.Inject;
import commons.Card;

public class ViewCardService {

    private final CardUtils server;
    private String boardKey;
    @Inject
    public ViewCardService(CardUtils server) {
        this.server = server;
    }

    public void deleteCard(Card card)
    {
        //server.deleteFromCardList(card);
        //server.deleteCard(card.getId());
    }

    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }
}
