package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    private CardList cardList;

    private CardList cardList1;

    private ArrayList<Card> cards;

    private Card card;

    @BeforeEach
    void setUp() {
        cardList = new CardList("My Card List", new ArrayList<>());
        card = new Card(
                "My Card",
                "Text",
                "White",
                null,
                null
        );
        cards = new ArrayList<>();
        cards.add(card);
        cardList1 = new CardList("My Card List", cards);
    }

    @Test
    void constructor() {
        CardList cardList3 = new CardList("My Card List", new ArrayList<>());
        assertNotNull(cardList3);
    }

    @Test
    void addCard() {
        cardList.addCard(card);
        assertEquals(card, cardList.getCard(0));
    }

    @Test
    void addCardByIndex() {
        cardList.addCard(card, 0);
        assertEquals(card, cardList.getCard(0));
    }

    @Test
    void removeCardSuccessful() {
        cardList1.removeCard(card);
        assertEquals(new ArrayList<>(), cardList1.getCards());
    }

    @Test
    void removeCardUnsuccessful() {
        assertEquals(null, cardList1.removeCard(new Card()));
    }

    @Test
    void removeCardByIndex() {
        cardList1.removeCard(0);
        assertEquals(new ArrayList<>(), cardList1.getCards());
    }

    @Test
    void removeCardByIndexUnsuccessful() {
        assertEquals(null, cardList1.removeCard(10));
    }

    @Test
    void getCard() {
        assertEquals(card, cardList1.getCard(0));
    }

    @Test
    void moveCard() {
        Card card2 = new Card(
                "My Card 2",
                "Text",
                "White",
                null,
                null
        );
        cardList1.addCard(card2);
        cardList1.moveCard(card, 1);
        assertEquals(card, cardList1.getCard(1));
    }

    @Test
    void testMoveCard() {
        Card card2 = new Card(
                "My Card 2",
                "Text",
                "White",
                null,
                null
        );
        cardList1.addCard(card2);
        cardList1.moveCard(0, 1);
        assertEquals(card, cardList1.getCard(1));
    }

    @Test
    void getId() {
        assertNotNull(cardList.getId());
    }

    @Test
    void getListTitle() {
        assertEquals("My Card List", cardList.getListTitle());
    }

    @Test
    void getCards() {
        assertEquals(cards, cardList1.getCards());
    }

    @Test
    void setId() {
        cardList.setId(0);
        assertEquals(0, cardList.getId());
    }

    @Test
    void setListTitle() {
        cardList.setListTitle("Title");
        assertEquals("Title", cardList.getListTitle());
    }

    @Test
    void setCards() {
        cardList.setCards(cards);
        assertEquals(cards, cardList.getCards());
    }
}