package client.windows.lists.list;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import client.serverUtils.ServerUtils;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class ListServiceTest {
    CardList testList;
    Card testCard1;
    private ListService listService;
    @BeforeEach
    void beforeEach()
    {
        listService = new ListService(new CardListUtils(new ServerUtils()),new CardUtils(new ServerUtils()),new BoardUtils(new ServerUtils()));
        testList = new CardList();
        testList.setListTitle("Test List");
        testList.setId(2);

        testCard1 = new Card("Test Card1","Test Description",new ArrayList<>(),new ArrayList<>(),1);
        Card testCard2 = new Card("Test Card2","Test Description",new ArrayList<>(),new ArrayList<>(),2);

        testList.addCard(testCard1);
        testList.addCard(testCard2);
        listService.setCardList(testList);
    }

    @Test
    void refreshCardList() {
        assertEquals(listService.getCardList(),testList);
    }

    @Test
    void getCardList() {
        assertEquals(listService.getCardList(),testList);
    }

    @Test
    void setCardList() {
        testList.addCard(new Card("New Card"));
        listService.setCardList(new CardList());
        assertNotEquals(listService.getCardList(),testList);
        listService.setCardList(testList);
        assertEquals(listService.getCardList(),testList);

        CardList test = new CardList();
        test.setListTitle("Test2");
        listService.setCardList(test);
        assertNotEquals(listService.getCardList(),testList);
        assertEquals(listService.getCardList(),test);
    }

    @Test
    void renameCardList() {
      //TODO
    }

    @Test
    void deleteFromCardList() {
        //TODO
    }

    @Test
    void testGetCardList() {
        //TODO
    }

    @Test
    void dragAndDrop() {
        //TODO
    }


}
