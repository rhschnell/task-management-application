package server.features.boards;

import commons.Board;
import commons.CardList;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
        sut = new BoardController(new BoardService(repository),new SimpMessagingTemplate(new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return false;
            }
        }));
    }

    @Test
    void insertSuccess() {
        sut.setTesting(true);
        CardList cardList = new CardList();
        List<CardList> cardLists = new ArrayList<>();
        cardLists.add(cardList);
        Board board = new Board("000000", "My Board", cardLists, null);
        board.addTag(new Tag("New Tag","White"));
        ResponseEntity<Board> response = sut.insert(board);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(board, sut.getById("000000").getBody());
    }

    @Test
    void insertBadRequest() {
        sut.setTesting(true);
        assertEquals(HttpStatus.BAD_REQUEST, sut.insert(null).getStatusCode());
    }

    @Test
    void getBoardTags() {
        sut.setTesting(true);
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
        sut.setTesting(true);
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
        sut.setTesting(true);
        Board board1 = new Board();
        Board board2 = new Board();

        sut.insert(board1);
        sut.insert(board2);

        assertEquals(List.of(board1, board2), sut.getAll().getBody());
    }

    @Test
    void getByIdSuccess() {
        sut.setTesting(true);
        Board myBoard = new Board("any key", "some title", new LinkedList<>(), null);

        ResponseEntity<Board> response = sut.insert(myBoard);
        Board saved = sut.getById(myBoard.getKey()).getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(myBoard, saved);
    }

    @Test
    void getByIdNotFound() {
        sut.setTesting(true);
        ResponseEntity<Board> foundById = sut.getById("1");

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void getByIdBadRequest() {
        sut.setTesting(true);
        ResponseEntity<Board> foundById = sut.getById(null);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        sut.setTesting(true);
        ResponseEntity<Void> response = sut.delete("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteBadRequest() {
        sut.setTesting(true);
        ResponseEntity<Void> response = sut.delete(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        sut.setTesting(true);
        Board board1 = new Board("1", "Title", new ArrayList<>(), null);
        Board board2 = new Board("2", "Title", new ArrayList<>(), null);

        sut.insert(board1);
        sut.insert(board2);
        ResponseEntity<Void> response = sut.delete("2");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(board1), repository.getBoards());
    }
}
