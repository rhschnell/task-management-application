package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.features.cardlists.CardListController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardListControllerTest {

    private TestCardListRepository repository;
    private CardListController sut;

    @BeforeEach
    void before() {
        repository = new TestCardListRepository();
        sut = new CardListController(repository);
    }

    @Test
    void add() {
        Card card = new Card(
                "My Card",
                "Text",
                "White",
                null,
                null
        );
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);
        CardList cardList = new CardList("My Card List", cards);

        ResponseEntity<CardList> added = sut.add(cardList);
        assert repository.getCalledMethods().contains("save");
        assertEquals(cardList, added.getBody());
    }

    @Test
    void getAll() {
        CardList cardList1 = new CardList();
        CardList cardList2 = new CardList();

        sut.add(cardList1);
        sut.add(cardList2);

        List<CardList> actual = sut.findAll();
        assert repository.getCalledMethods().contains("findAll");

        List<CardList> expected = List.of(cardList1, cardList2);
        assertEquals(expected, actual);
    }

    @Test
    void getByIdSuccess() {
        CardList myCardList = new CardList();
        CardList saved = sut.add(myCardList).getBody();
        long assignedId = saved.getId();

        CardList returned = sut.getById(assignedId).getBody();
        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("getById");
        assertEquals(myCardList, returned);
    }

    @Test
    void getByIdFailure() {
        ResponseEntity<CardList> foundById = sut.getById(1);
        assert repository.getCalledMethods().contains("existsById");
        assert !repository.getCalledMethods().contains("getById");
        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<String> response = sut.delete(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        CardList cardList1 = new CardList();
        CardList cardList2 = new CardList();

        sut.add(cardList1);
        sut.add(cardList2);

        ResponseEntity<String> response = sut.delete(2);

        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("deleteById");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted the card list with id 2", response.getBody());
        assertEquals(List.of(cardList1), repository.getCardLists());

    }
}