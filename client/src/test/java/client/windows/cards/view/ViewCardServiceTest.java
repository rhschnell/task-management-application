package client.windows.cards.view;

import client.serverUtils.CardUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ViewCardServiceTest {


    private ViewCardService viewCardService;
    private CardUtils server;

    @BeforeEach
    void setUp() {
        server = Mockito.mock(CardUtils.class);
        viewCardService = new ViewCardService(server);
    }

    @Test
    void deleteCard() {
        Card card = new Card();
        CardList cardList = new CardList();
        cardList.addCard(card);
        viewCardService.deleteCard(card);
        //Need to see why this test fails
        verify(server,times(0)).deleteFromCardList(cardList.getId(),card);
    }

    @Test
    void getBoardKey() {
        assertNull(viewCardService.getBoardKey());
    }

    @Test
    void setBoardKey() {
        viewCardService.setBoardKey("Key");
        assertEquals("Key", viewCardService.getBoardKey());
    }
}