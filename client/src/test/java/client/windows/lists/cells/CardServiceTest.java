package client.windows.lists.cells;

import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CardServiceTest {
    private CardService cardService;

    private CardUtils cardUtilsMock;

    private CardListUtils cardListUtilsMock;

    @BeforeEach
    public void setup() {
        cardListUtilsMock = mock(CardListUtils.class);
        cardUtilsMock = mock(CardUtils.class);
        cardService = new CardService(cardUtilsMock, cardListUtilsMock);
    }

    @Test
    public void deleteCard() {
        Card card = new Card();
        card.setId(1L);
        cardService.deleteCard(card);
        verify(cardUtilsMock).deleteCard(1L);
        verify(cardUtilsMock).deleteCardFromDatabase(1L);
    }

    @Test
    public void updateCard() {
        Card card = new Card();
        card.setId(1L);
        cardService.updateCard(card);
        verify(cardUtilsMock).insertCard(card);
    }

    @Test
    public void insertCardListTest() {
        CardList cardList = new CardList();
        cardService.insertCardList(cardList);
        verify(cardListUtilsMock).insertCardList(cardList);
    }

    @Test
    public void getCardByIdTest() {
        Card card = new Card();
        card.setId(1L);
        when(cardUtilsMock.getCardById(1L)).thenReturn(card);
        assertEquals(card, cardService.getCardByID(1L));
    }
}
