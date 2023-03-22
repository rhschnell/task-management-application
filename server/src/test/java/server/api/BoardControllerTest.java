package server.api;

import commons.Board;
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
import server.database.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardControllerTest {

    private class TestBoardRepository implements BoardRepository {
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
        public <S extends Board, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        private Optional<Board> find(String key) {
            return boards.stream().filter(board -> board.getKey().equals(key)).findFirst();
        }
    }

    private TestBoardRepository repository;
    private BoardController sut;

    @BeforeEach
    void before() {
        repository = new TestBoardRepository();
        sut = new BoardController(repository);
    }

    @Test
    void add() {
        CardList cardList = new CardList();
        ArrayList<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists);

        ResponseEntity<Board> added = sut.add(board);
        assert repository.getCalledMethods().contains("save");
        assertEquals(board, added.getBody());
    }

    @Test
    void getAll() {
        Board board1 = new Board();
        Board board2 = new Board();

        sut.add(board1);
        sut.add(board2);

        List<Board> actual = sut.findAll();
        assert repository.getCalledMethods().contains("findAll");

        List<Board> expected = List.of(board1, board2);
        assertEquals(expected, actual);
    }

    @Test
    void getByIdSuccess() {
        Board myBoard = new Board();
        Board saved = sut.add(myBoard).getBody();
        String assignedId = saved.getKey();

        Board returned = sut.getById(assignedId).getBody();
        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("getById");
        assertEquals(myBoard, returned);
    }

    @Test
    void getByIdFailure() {
        ResponseEntity<Board> foundById = sut.getById("1");
        assert repository.getCalledMethods().contains("existsById");
        assert !repository.getCalledMethods().contains("getById");
        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<String> response = sut.delete("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Board board1 = new Board();
        Board board2 = new Board();

        sut.add(board1);
        sut.add(board2);

        ResponseEntity<String> response = sut.delete("2");

        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("deleteById");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted the board with key 2", response.getBody());
        assertEquals(List.of(board1), repository.getBoards());

    }
}