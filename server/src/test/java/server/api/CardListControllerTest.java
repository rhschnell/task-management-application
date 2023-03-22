package server.api;

import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.CardListRepository;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CardListControllerTest {

    private class TestCardListRepository implements CardListRepository {
        private final List<CardList> cardLists = new ArrayList<>();
        private final List<String> calledMethods = new ArrayList<>();

        public List<CardList> getCardLists() {
            return cardLists;
        }

        public List<String> getCalledMethods() {
            return calledMethods;
        }

        private void call(String name) {
            calledMethods.add(name);
        }

        @Override
        public List<CardList> findAll() {
            calledMethods.add("findAll");
            return cardLists;
        }

        @Override
        public List<CardList> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<CardList> findAll(Pageable pageable) {
            return null;
        }

        @Override
        public List<CardList> findAllById(Iterable<Long> longs) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long id) {
            call("deleteById");
            cardLists.removeIf(entity -> entity.getId() == id);
        }

        @Override
        public void delete(CardList entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {

        }

        @Override
        public void deleteAll(Iterable<? extends CardList> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public <S extends CardList> S save(S entity) {
            call("save");
            entity.setId(cardLists.size() + 1);
            cardLists.add(entity);
            return entity;
        }

        @Override
        public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<CardList> findById(Long aLong) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Long id) {
            call("existsById");
            return find(id).isPresent();
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends CardList> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<CardList> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public CardList getOne(Long aLong) {
            return null;
        }

        @Override
        public CardList getById(Long id) {
            call("getById");
            return find(id).get();
        }

        private Optional<CardList> find(Long id) {
            return cardLists.stream().filter(q -> q.getId() == id).findFirst();
        }

        @Override
        public <S extends CardList> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends CardList> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends CardList> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends CardList> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends CardList> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends CardList, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

    }

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