package server.api;

import commons.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class TestBoardRepository implements BoardRepository {
    private final List<Board> boards = new ArrayList<>();
    private final List<String> calledMethods = new ArrayList<>();

    public List<Board> getBoards() {
        return boards;
    }

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Board> findAll() {
        calledMethods.add("findAll");
        return boards;
    }

    @Override
    public List<Board> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Board> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String key) {
        call("deleteById");
        boards.removeIf(board -> key.equals(board.getKey()));
    }

    @Override
    public void delete(Board entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Board> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Board> S save(S entity) {
        call("save");
        entity.setKey(Integer.toString(boards.size() + 1));
        boards.add(entity);
        return entity;
    }

    @Override
    public <S extends Board> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Board> findById(String s) {
        return Optional.empty();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Board> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Board> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Board> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Board getOne(String s) {
        return null;
    }

    @Override
    public boolean existsById(String key) {
        call("existsById");
        return find(key).isPresent();
    }

    @Override
    public Board getById(String key) {
        call("getById");
        return find(key).get();
    }

    @Override
    public <S extends Board> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Board> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Board> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Board> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Board> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Board, R> R findBy(Example<S> example,
                                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    private Optional<Board> find(String key) {
        return boards.stream().filter(board -> board.getKey().equals(key)).findFirst();
    }
}
