package server.api;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.features.boards.BoardController;
import server.features.boards.BoardService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BoardControllerTest {

    private TestBoardRepository repository;
    private BoardController sut;

    @BeforeEach
    void before() { // THIS NEEDS TO BE FIXED
        repository = new TestBoardRepository();
        sut = new BoardController(new BoardService(repository)); // TODO
    }

    @Test
    void add() {
        CardList cardList = new CardList();
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists);

        sut.insert(board);

        assert repository.getCalledMethods().contains("save");
        assertEquals(board, sut.getById("000000").getBody());
    }

    @Test
    void getAll() {
        Board board1 = new Board();
        Board board2 = new Board();

        sut.insert(board1);
        sut.insert(board2);

        List<Board> actual = sut.getAll().getBody();
        assert repository.getCalledMethods().contains("findAll");

        List<Board> expected = List.of(board1, board2);
        assertEquals(expected, actual);
    }

    @Test
    void getByIdSuccess() {
        Board myBoard = new Board("any key", "some title", new LinkedList<>());
        sut.insert(myBoard);
        Board saved = sut.getById(myBoard.getKey()).getBody();
        assert saved != null;
        String assignedId = saved.getKey();

        Board returned = sut.getById(assignedId).getBody();
        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("getById");
        assertSame(myBoard, returned);
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
        ResponseEntity<Void> response = sut.delete("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Board board1 = new Board("1", "Title", new ArrayList<>());
        Board board2 = new Board("2", "Title", new ArrayList<>());

        sut.insert(board1);
        sut.insert(board2);

        ResponseEntity<Void> response = sut.delete("2");

        assert repository.getCalledMethods().contains("existsById");
        assert repository.getCalledMethods().contains("deleteById");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(board1), repository.getBoards());
    }
}