package server.boards;

import commons.Board;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.BOARD)
public class BoardController {
    private BoardService service;

    /**
     * Creates a new BoardController
     *
     * @param service Instance of board repository
     */
    public BoardController(BoardService service) {
        this.service = service;
    }

    /**
     * Adds a board to the database
     *
     * @param board The board to add
     * @return The added board
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> add(@RequestBody Board board) {
        return ResponseEntity.ok(service.addBoard(board));
    }

    /**
     * Gets all boards from the database
     *
     * @return List of all boards in the database
     */
    @GetMapping(path = {"", "/"})
    public List<Board> findAll() {
        return boards.findAll();
    }

    /**
     * Gets a specific board from the database
     *
     * @param key The key of the board
     * @return If the board was not found, status code 400,
     * else status code 200 and data in the body
     */
    @GetMapping("/{key}")
    public ResponseEntity<Board> getById(@PathVariable("key") String key) {
        if (!boards.existsById(key)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boards.getById(key));
    }

    /**
     * Deletes a board from the database
     *
     * @param key The key of the board to delete
     * @return A successful response message if the board was found, otherwise a 404 not found
     * response
     */
    @DeleteMapping("/{key}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> delete(@PathVariable("key") String key) {
        if (!boards.existsById(key)) return ResponseEntity.notFound().build();
        boards.deleteById(key);
        return ResponseEntity.ok(String.format("Successfully deleted the board with key %s", key));
    }
}
