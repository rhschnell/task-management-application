package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {
    private TestCardRepository repo;

    private CardController sut;

    @BeforeEach
    public void setup() {
        repo = new TestCardRepository();
        sut = new CardController(repo);
    }


    @Test
    void add() {
        Card toAdd = new Card("A card", "This is a card", "White",
                null, null, 3);

        ResponseEntity<Card> added = sut.add(toAdd);
        assert repo.getCalledMethods().contains("save");
        assertEquals(toAdd, added.getBody());
    }

    @Test
    void findAll() {
        Card card1 = new Card("Card 1", "This is a card", "White",
                null, null, 0);
        Card card2 = new Card("Card2", "This is a card", "White",
                null, null, 0);
        Card card3 = new Card("Card 3", "This is a card", "White",
                null, null, 0);

        sut.add(card1);
        sut.add(card2);
        sut.add(card3);

        List<Card> actual = sut.findAll();
        assert repo.getCalledMethods().contains("findAll");

        List<Card> expected = List.of(card1, card2, card3);
        assertEquals(expected, actual);

    }

    @Test
    void getById() {
        Card card1 = new Card();
        Card saved = sut.add(card1).getBody();
        long assignedId = saved.getId();

        Card foundById = sut.getById(assignedId).getBody();
        assert repo.getCalledMethods().contains("getById");
        assertEquals(card1, foundById);
    }

    @Test
    void delete() {
    }
}