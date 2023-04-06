package server.features.boards;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest {

    private TestBoardRepository repository;
    private BoardController sut;

    @BeforeEach
    void before() {
        repository = new TestBoardRepository();
        sut = new BoardController(new BoardService(repository));
    }

    @Test
    void insertSuccess() {
        CardList cardList = new CardList();
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists, null);
        board.addTag(new Tag("New Tag","White"));
        ResponseEntity<Void> response = sut.insert(board);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, sut.getById("000000").getBody());
    }

    @Test
    void insertBadRequest() {
        assertEquals(HttpStatus.BAD_REQUEST, sut.insert(null).getStatusCode());
    }

    @Test
    void getBoardTags() {
        CardList cardList = new CardList();
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists, null);
        board.addTag(new Tag("New Tag","White"));
        sut.insert(board);
        ArrayList <Tag> tagResult = new ArrayList<>();
        tagResult.add(new Tag("New Tag","White"));
        assertEquals(tagResult,sut.getBoardTags("000000").getBody());
    }
    @Test
    void addBoardTag() {
        CardList cardList = new CardList();
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists, null);
        board.addTag(new Tag("First Tag","White"));
        sut.insert(board);
        ArrayList <Tag> tagResult = new ArrayList<>();
        tagResult.add(new Tag("First Tag","White"));
        tagResult.add(new Tag("Second Tag","Black"));
        sut.addBoardTag(board.getKey(), new Tag("Second Tag","Black"));
        assertEquals(tagResult,sut.getBoardTags("000000").getBody());
    }

    @Test
    void getAll() {
        Board board1 = new Board();
        Board board2 = new Board();

        sut.insert(board1);
        sut.insert(board2);

        assertEquals(List.of(board1, board2), sut.getAll().getBody());
    }

    @Test
    void getByIdSuccess() {
        Board myBoard = new Board("any key", "some title", new LinkedList<>(), null);

        ResponseEntity<Void> response = sut.insert(myBoard);
        Board saved = sut.getById(myBoard.getKey()).getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(myBoard, saved);
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Board> foundById = sut.getById("1");

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void getByIdBadRequest() {
        ResponseEntity<Board> foundById = sut.getById(null);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<Void> response = sut.delete("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteBadRequest() {
        ResponseEntity<Void> response = sut.delete(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Board board1 = new Board("1", "Title", new ArrayList<>(), null);
        Board board2 = new Board("2", "Title", new ArrayList<>(), null);

        sut.insert(board1);
        sut.insert(board2);
        ResponseEntity<Void> response = sut.delete("2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(board1), repository.getBoards());
    }
}