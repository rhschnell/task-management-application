package server.features.boards;

import commons.Board;
import commons.Route;
import commons.Tag;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping(Route.BOARD)
public class BoardController {
    private final BoardService service;
    private final SimpMessagingTemplate sender;
    private Boolean testing = false;

    /**
     * Disables websockets for testing
     * @param testing
     */
    public void setTesting(Boolean testing) {
        this.testing = testing;
    }
    /**
     * Creates a new BoardController
     *
     * @param service Instance of board repository
     * @param sender
     */
    public BoardController(BoardService service, SimpMessagingTemplate sender) {
        this.service = service;
        this.sender = sender;
    }


    /**
     * Adds a board to the database
     *
     * @param board The board to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Board> insert(@RequestBody Board board) {
        try {
            Board inserted = service.insert(board);
            if(!testing)
                sender.convertAndSend("/topic/boards/"+board.getKey(),inserted);
            return ResponseEntity.ok(inserted);
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
     * Gets a list of all the tags associated to a board in the database
     * @param key The key of the board
     * @return List of associated board tags
     */
    @GetMapping("/getBoardTags/{key}")
    public ResponseEntity<List<Tag>> getBoardTags(@PathVariable("key") String key) {
        try {
            List<Tag> returnTags = service.getByID(key).getTagList();

            return ResponseEntity.ok(returnTags);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    private final Map<String, List<Pair<Object, Consumer<Pair<String, Tag>>>>> listeners  = new HashMap<>();


    /**
     * Adds a tag to a board in the database
     * @param key The key of the board to add the tag to
     * @param tag The tag to add to the board
     * @return The freshly added tag
     */
    @PostMapping("/addBoardTag/{key}")
    public ResponseEntity<Tag> addBoardTag(@PathVariable("key") String key, @RequestBody Tag tag) {
        try {
            Board updateBoard = service.getByID(key);
            updateBoard.addTag(tag);
            service.insert(updateBoard);
            Pair<String, Tag> updatePair = Pair.of("Add",
                    updateBoard.getTagList().get(updateBoard.getTagList().size()-1));
            if (listeners.get(key) == null) {return null;} // never happens in practice, but only in test
            for(int i = 0;i<listeners.get(key).size();i++)
            {
                if( listeners.get(key).get(i)!=null)
                            listeners.get(key).get(i).getSecond().accept(updatePair);
            }

            return ResponseEntity.ok(tag);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Removes a tag from a board
     * @param key The board the remove the tag from
     * @param tag The tag to remove from the board
     * @return The removed tag
     */
    @PostMapping("/removeBoardTag/{key}")
    public synchronized ResponseEntity<Tag> removeBoardTag(@PathVariable("key") String key,@RequestBody Tag tag) {
        try {
            Pair<String, Tag> removePair = Pair.of("Remove", tag);
            if (listeners.get(key) == null) {return null;} // never happens in practice, but only in test
            for(int i = 0;i<listeners.get(key).size();i++)
            {
                if( listeners.get(key).get(i)!=null)
                      listeners.get(key).get(i).getSecond().accept(removePair);
            }
            return ResponseEntity.ok(tag);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Methods for clients to register for tag updates for boards
     * @param key The key of the board to register for
     * @return Updated tag if there are any updates within 5 seconds, otherwise response with
     * NO_CONTENT code
     */
    @GetMapping("/{key}/tagUpdates")
    public synchronized DeferredResult<ResponseEntity<Pair<String,Tag>>> getTagUpdates(@PathVariable("key")
                                                                                           String key) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Pair<String,Tag>>>(5000L, noContent);

        listeners.computeIfAbsent(key, k -> new ArrayList<>());

        Object o = new Object();
        listeners.get(key).add(Pair.of(o, t -> res.setResult(ResponseEntity.ok(t))));

        res.onCompletion(() -> {
            listeners.get(key).removeIf(pair -> pair.getFirst().equals(o));
        });

        return res;
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
