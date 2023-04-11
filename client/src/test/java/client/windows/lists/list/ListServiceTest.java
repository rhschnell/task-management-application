package client.windows.lists.list;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import client.serverUtils.ServerUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ListServiceTest {
    @InjectMocks
    private ListService listService;

    @Mock
    private CardListUtils cardListUtilsMock;

    @Mock
    private CardUtils cardServerMock;

    @Mock
    private BoardUtils boardUtilsMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        listService.setBoardKey("Test key");
    }

    @Test
    public void renameCardList() {
        String listName = "New List Name";
        CardList cardList = new CardList();
        cardList.setId(0L);
        cardList.setListTitle("New List Name");
        when(cardListUtilsMock.getCardList(cardList.getId())).thenReturn(cardList);

        listService.renameCardList(listName);

        verify(cardListUtilsMock).insertCardList(cardList);
        assertEquals(listName, cardList.getListTitle());
    }

    @Test
    public void deleteFromCardList() {
        Card card = new Card();
        card.setId(0L);

        listService.deleteFromCardList(card);

        verify(cardServerMock).deleteCard(card.getId());
        verify(cardServerMock).deleteCardFromDatabase(card.getId());
    }

    @Test
    public void getCardList() {
        long listId = 1L;
        CardList cardList = new CardList();
        cardList.setId(listId);

        when(cardListUtilsMock.getCardList(listId)).thenReturn(cardList);
        CardList result = listService.getCardList(listId);

        assertEquals(cardList, result);
    }

    @Test
    public void dragAndDrop() {
        CardListUtils listServerMock = Mockito.mock(CardListUtils.class);
        CardUtils cardServerMock = Mockito.mock(CardUtils.class);
        BoardUtils boardUtilsMock = Mockito.mock(BoardUtils.class);

        ListService listService = new ListService(listServerMock, cardServerMock, boardUtilsMock);

        Card card = new Card();
        card.setId(1L);
        card.setPriority(1);
        card.setDescription("Some card Description");

        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setListTitle("Some list title");
        cardList.addCard(card);

        listService.setCardList(cardList);

        when(listServerMock.getCardList(1L)).thenReturn(cardList);
        listService.dragAndDrop(card, 0);

        assertTrue(cardList.getCards().contains(card));
        assertEquals(card, cardList.getCards().get(0));
        verify(listServerMock).insertCardList(cardList);
    }
}
