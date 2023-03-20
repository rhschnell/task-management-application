package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

@RestController
@RequestMapping(Route.BOARD)
public class BoardController {
    private BoardRepository boards;

    /**
     * Creates a new BoardController
     * @param boards Instance of board repository
     */
    public BoardController (BoardRepository boards)
    {
        this.boards = boards;
    }

    /**
     * Adds a board to the database
     * @param board The board to add
     * @return The added board
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> add(@RequestBody Board board)
    {
        return ResponseEntity.ok(boards.save(board));
    }

    /**
     * Gets all boards from the database
     * @return List of all boards in the database
     */
    @GetMapping(path = {"", "/"})
    public List<Board> getAll()
    {
        return boards.findAll();
    }

    /**
     * Gets a specific board from the database
     * @param id ID of the board
     * @return If the board was not found, status code 400, else status code 200 and data in the body
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") String id) {
        if(!boards.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boards.findById(id).get());
    }

    /**
     * Deletes a board from the database
     * @param id The id of the board to delete
     * @return The deleted board if it was found, otherwise a bad request response
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") String id) throws JsonProcessingException
    {
        if (!boards.existsById(id))
        {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<Board> response = getById(id);

        if (response.getStatusCode() != HttpStatus.OK)
        {
            return response;
        }

        Board deleted = response.getBody();
        boards.delete(deleted);

        if (deleted == null) throw new IllegalStateException("Why is this null?");
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(deleted));
    }
}
