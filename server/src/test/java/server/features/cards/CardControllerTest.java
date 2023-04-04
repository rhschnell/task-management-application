package server.features.cards;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private TestCardRepository repo;

    private CardController sut;

    @BeforeEach
    public void setup() {
        repo = new TestCardRepository();
        sut = new CardController(new CardService(repo));
    }


    @Test
    void insertInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST, sut.insert(null).getStatusCode());
    }

    @Test
    void insertValid() {
        Card toAdd = new Card("A card", "This is a card", 
                null, null, 3);

        ResponseEntity<Void> response = sut.insert(toAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toAdd, repo.getById(toAdd.getId()));
    }

    @Test
    void findAll() {
        Card card1 = new Card("Card 1", "This is a card", 
                null, null, 0);
        Card card2 = new Card("Card2", "This is a card", 
                null, null, 0);

        sut.insert(card1);
        sut.insert(card2);
        List<Card> actual = sut.getAll().getBody();
        List<Card> expected = List.of(card1, card2);

        assertEquals(expected, actual);

    }

    @Test
    void getByIdSuccess() {
        Card card = new Card();
        ResponseEntity<Void> response = sut.insert(card);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(card, sut.getById(card.getId()).getBody());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Card> foundById = sut.getById(100);

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void getByIdBadRequest() {
        ResponseEntity<Card> foundById = sut.getById(-1);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<Void> response = sut.delete(100);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteBadRequest() {
        ResponseEntity<Void> response = sut.delete(-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Card card = new Card(
                "My Card",
                "Chocolate",
                new ArrayList<>(),
                new ArrayList<>()
                );

        sut.insert(card);
        ResponseEntity<Void> response = sut.delete(card.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repo.getCards().contains(card));
    }
}
