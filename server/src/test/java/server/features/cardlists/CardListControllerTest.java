package server.features.cardlists;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardListControllerTest {

    private TestCardListRepository repository;
    private CardListController cardListController;

    @BeforeEach
    void before() {
        repository = new TestCardListRepository();
        cardListController = new CardListController(new CardListService(repository));
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

        cardListController.insert(cardList);

        assertEquals(cardList, cardListController.getById(cardList.getId()).getBody());
    }

    @Test
    void insertInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST, cardListController.insert(null).getStatusCode());
    }

    @Test
    void getAll() {
        CardList cardList1 = new CardList("Card List 1",  new ArrayList<>());
        CardList cardList2 = new CardList("Card List 2",  new ArrayList<>());
        List<CardList> expected = List.of(cardList1, cardList2);

        cardListController.insert(cardList1);
        cardListController.insert(cardList2);
        List<CardList> actual = cardListController.getAll().getBody();

        assertEquals(expected, actual);
    }

    @Test
    void getByIdSuccess() {
        CardList myCardList = new CardList("Card List",  new ArrayList<>());

        cardListController.insert(myCardList);
        CardList returned = cardListController.getById(myCardList.getId()).getBody();

        assertEquals(myCardList, returned);
    }

    @Test
    void getByIdBadRequest() {
        ResponseEntity<CardList> foundById = cardListController.getById(-1);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<CardList> foundById = cardListController.getById(100);

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void deleteBadRequest() {
        ResponseEntity<Void> response = cardListController.delete(-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteNotFound() {
        ResponseEntity<Void> response = cardListController.delete(100);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        CardList myCardList = new CardList("Card List",  new ArrayList<>());

        cardListController.insert(myCardList);
        ResponseEntity<Void> response = cardListController.delete(myCardList.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repository.getCardLists().contains(myCardList));
    }

    @Test
    void removeFromCardList()
    {
        CardListService service = new CardListService(repository);
        CardList cardList = new CardList();
        Card card = new Card();
        cardList.addCard(card);
        repository.save(cardList);

        assertEquals(card, service.removeFromCardList(card));
    }

    @Test
    void removeFromCardListController()
    {
        CardList cardList = new CardList();
        Card card = new Card();
        cardList.addCard(card);
        repository.save(cardList);

        assertEquals(HttpStatus.OK, cardListController.removeFromCardList(card).getStatusCode());
    }

    @Test
    void removeFromCardListControllerV2()
    {
        CardList cardList = new CardList();
        Card card = new Card();
        cardList.addCard(card);
        repository.save(cardList);

        assertEquals(card, cardListController.removeFromCardList(card).getBody());
    }

    @Test
    void removeFromCardListControllerIllegalArgument()
    {
        assertEquals(HttpStatus.BAD_REQUEST, cardListController.removeFromCardList(null).getStatusCode());
    }

    @Test
    void removeFromCardListNotFound()
    {
        Card card = new Card();
        assertEquals(HttpStatus.NOT_FOUND, cardListController.removeFromCardList(card).getStatusCode());
    }
}