package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    private Board board2;

    private CardList cardList;

    @BeforeEach
    void setUp() {
        board = new Board("000000", "My Board", null, null);
        cardList = new CardList();
        Card card = new Card(
                "My Card",
                "Text",
                "White",
                null,
                null
        );
        cardList.addCard(card);
        board2 = new Board("000001", "My Board", null, null);
        board2.addList(cardList);
    }

    @Test
    void NotEmptyConstructorTest() {
        assertNotNull(board);
    }

    @Test
    void EmptyConstructorTest() {
        assertNotNull(new Board());
    }

    @Test
    void addNewList() {
        CardList cardList = new CardList();
        board.addList();
        assertEquals(cardList, board.getCardLists().get(0));
    }

    @Test
    void testAddMadeList() {
        CardList cardList = new CardList();
        Card card = new Card(
                "My Card",
                "Text",
                "White",
                null,
                null
        );
        cardList.addCard(card);
        board.addList(cardList);
        assertEquals(cardList, board.getCardLists().get(0));
    }

    @Test
    void removeList() {
        board2.removeList(cardList);
        assertEquals(new ArrayList<>(), board2.getCardLists());
    }

    @Test
    void removeListByIndex() {
        board2.removeListByIndex(0);
        assertEquals(new ArrayList<>(), board2.getCardLists());
    }

    @Test
    void getAmountList() {
        assertEquals(1, board2.getAmountList());
    }

    @Test
    void getKey() {
        assertEquals("000000", board.getKey());
    }

    @Test
    void getTitle() {
        assertEquals("My Board", board.getTitle());
    }

    @Test
    void getCardLists() {
        assertEquals(new ArrayList<>(), board.getCardLists());
    }

    @Test
    void setKey() {
        board.setKey("000002");
        assertEquals("000002", board.getKey());
    }

    @Test
    void setTitle() {
        board.setTitle("New Title");
        assertEquals("New Title", board.getTitle());
    }
}