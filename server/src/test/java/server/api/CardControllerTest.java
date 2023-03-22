package server.api;

import commons.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.CardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    private class TestCardRepository implements CardRepository {

        private final List<Card> cards = new ArrayList<>();
        private final List<String> calledMethods = new ArrayList<>();

        public List<Card> getCards() {
            return cards;
        }

        public List<String> getCalledMethods() {
            return calledMethods;
        }

        private void call(String name) {
            calledMethods.add(name);
        }

        @Override
        public List<Card> findAll() {
            calledMethods.add("findAll");
            return cards;
        }

        @Override
        public List<Card> findAll(Sort sort) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<Card> findAllById(Iterable<Long> ids) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> List<S> saveAll(Iterable<S> entities) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void flush() {
            // TODO Auto-generated method stub

        }

        @Override
        public <S extends Card> S saveAndFlush(S entity) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<Card> entities) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> ids) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deleteAllInBatch() {
            // TODO Auto-generated method stub

        }

        @Override
        public Card getOne(Long id) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Card getById(Long id) {
            call("getById");
            return find(id).get();
        }

        private Optional<Card> find(Long id) {
            return cards.stream().filter(q -> q.getId() == id).findFirst();
        }

        @Override
        public <S extends Card> List<S> findAll(Example<S> example) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Page<Card> findAll(Pageable pageable) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> S save(S entity) {
            call("save");
            entity.setId(cards.size() + 1);
            cards.add(entity);
            return entity;
        }

        @Override
        public Optional<Card> findById(Long id) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean existsById(Long id) {
            call("existsById");
            return find(id).isPresent();
        }

        @Override
        public long count() {
            return cards.size();
        }

        @Override
        public void deleteById(Long id) {
            // TODO Auto-generated method stub
            call("deleteById");
            cards.removeIf(card -> card.getId() == id);
        }

        @Override
        public void delete(Card entity) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deleteAllById(Iterable<? extends Long> ids) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deleteAll(Iterable<? extends Card> entities) {
            // TODO Auto-generated method stub

        }

        @Override
        public void deleteAll() {
            // TODO Auto-generated method stub

        }

        @Override
        public <S extends Card> Optional<S> findOne(Example<S> example) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public <S extends Card> long count(Example<S> example) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public <S extends Card> boolean exists(Example<S> example) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <S extends Card, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            // TODO Auto-generated method stub
            return null;
        }
    }
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
    void getByIdSuccess() {
        Card card1 = new Card();
        Card saved = sut.add(card1).getBody();
        long assignedId = saved.getId();

        Card foundById = sut.getById(assignedId).getBody();
        assert repo.getCalledMethods().contains("existsById");
        assert repo.getCalledMethods().contains("getById");
        assertEquals(card1, foundById);
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Card> foundById = sut.getById(1);
        assert repo.getCalledMethods().contains("existsById");
        assert !repo.getCalledMethods().contains("getById");
        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<String> response = sut.delete(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Card card1 = new Card(); // This card will get id 1
        Card card2 = new Card(); // This card will get id 2

        sut.add(card1);
        sut.add(card2);

        ResponseEntity<String> response = sut.delete(2);

        assert repo.getCalledMethods().contains("existsById");
        assert repo.getCalledMethods().contains("deleteById");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted the card with id 2", response.getBody());
        assertEquals(List.of(card1), repo.getCards());

    }
}