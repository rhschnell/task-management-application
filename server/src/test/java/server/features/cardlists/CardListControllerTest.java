package server.features.cardlists;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardListControllerTest {

    private TestCardListRepository repository;
    private CardListController sut;

    @BeforeEach
    void before() {
        repository = new TestCardListRepository();
        sut = new CardListController(new CardListService(repository));
    }

    @Test
    void insertValid() {
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

        sut.insert(cardList);

        assertEquals(cardList, sut.getById(cardList.getId()).getBody());
    }

    @Test
    void insertInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST, sut.insert(null).getStatusCode());
    }

    @Test
    void getAll() {
        CardList cardList1 = new CardList("Card List 1",  new ArrayList<>());
        CardList cardList2 = new CardList("Card List 2",  new ArrayList<>());
        List<CardList> expected = List.of(cardList1, cardList2);

        sut.insert(cardList1);
        sut.insert(cardList2);
        List<CardList> actual = sut.getAll().getBody();

        assertEquals(expected, actual);
    }

    @Test
    void getByIdSuccess() {
        CardList myCardList = new CardList("Card List",  new ArrayList<>());

        sut.insert(myCardList);
        CardList returned = sut.getById(myCardList.getId()).getBody();

        assertEquals(myCardList, returned);
    }

    @Test
    void getByIdBadRequest() {
        ResponseEntity<CardList> foundById = sut.getById(-1);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<CardList> foundById = sut.getById(100);

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void deleteBadRequest() {
        ResponseEntity<Void> response = sut.delete(-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteNotFound() {
        ResponseEntity<Void> response = sut.delete(100);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        CardList myCardList = new CardList("Card List",  new ArrayList<>());

        sut.insert(myCardList);
        ResponseEntity<Void> response = sut.delete(myCardList.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repository.getCardLists().contains(myCardList));
    }
}