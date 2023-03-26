package server.features.boards;

import commons.Board;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.BOARD)
public class BoardController {
    private final BoardService service;

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
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody Board board) {
        try {
            service.insert(board);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a board from the database
     *
     * @param key The key of the board to delete
     * @return ResponseEntity with code 200 if successful or occurring error code
     *
     */
    @DeleteMapping("/{key}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("key") String key) {
        try {
            service.delete(key);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets a specific board from the database
     *
     * @param key The key of the board
     * @return ResponseEntity with code 200, containing the requested board, if successful or occurring error code
     */
    @GetMapping("/{key}")
    public ResponseEntity<Board> getById(@PathVariable("key") String key) {
        try {
            Board returnBoard = service.getByID(key);
            return ResponseEntity.ok(returnBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Gets all boards from the database
     *
     * @return ResponseEntity containing a list of all boards
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Board>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
