package server.features.cards;

import commons.Card;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class TestCardRepository implements CardRepository {

    private final List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public List<Card> findAll() {
        return cards;
    }

    @Override
    public List<Card> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<Card> findAllById(Iterable<Long> ids) {
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Card> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Card> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Card> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Card getOne(Long id) {
        return null;
    }

    @Override
    public Card getById(Long id) {
        return find(id).get();
    }

    private Optional<Card> find(Long id) {
        return cards.stream().filter(q -> q.getId() == id).findFirst();
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> S save(S entity) {
        entity.setId(cards.size() + 1);
        cards.add(entity);
        return entity;
    }

    @Override
    public Optional<Card> findById(Long id) {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return find(id).isPresent();
    }

    @Override
    public long count() {
        return cards.size();
    }

    @Override
    public void deleteById(Long id) {
        cards.removeIf(card -> card.getId() == id);
    }

    @Override
    public void delete(Card entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {

    }

    @Override
    public void deleteAll(Iterable<? extends Card> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Card> Optional<S> findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Card> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Card> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Card> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Card, R> R findBy(Example<S> example,
                                        Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
