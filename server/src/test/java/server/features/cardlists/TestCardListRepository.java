package server.features.cardlists;

import commons.CardList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class TestCardListRepository implements CardListRepository {
    private final List<CardList> cardLists = new ArrayList<>();

    public List<CardList> getCardLists() {
        return cardLists;
    }

    @Override
    public List<CardList> findAll() {
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
    public <S extends CardList, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

}
