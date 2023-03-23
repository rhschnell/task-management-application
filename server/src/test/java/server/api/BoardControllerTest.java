package server.api;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardControllerTest {

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