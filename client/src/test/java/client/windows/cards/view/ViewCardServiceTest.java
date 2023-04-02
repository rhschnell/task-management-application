package client.windows.cards.view;

import client.serverUtils.CardUtils;
import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        viewCardService.deleteCard(card);
        verify(server).deleteFromCardList(card);
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