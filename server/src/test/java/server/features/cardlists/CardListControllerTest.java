package server.features.cardlists;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CardListControllerTest {

    private TestCardListRepository repository;
    private CardListController cardListController;

    @BeforeEach
    void before() {
        repository = new TestCardListRepository();
        cardListController = new CardListController(new CardListService(repository),new SimpMessagingTemplate(new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        }));
    }

    @Test
    void insertValid() {
        cardListController.setTesting(true);
        Card card = new Card(
                "My Card",
                "Text",
                null,
                null,
                new ArrayList<>()
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
        cardListController.setTesting(true);
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
        cardListController.setTesting(true);
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
        cardListController.setTesting(true);
        CardList myCardList = new CardList("Card List",  new ArrayList<>());

        cardListController.insert(myCardList);
        ResponseEntity<Void> response = cardListController.delete(myCardList.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repository.getCardLists().contains(myCardList));
    }

}
