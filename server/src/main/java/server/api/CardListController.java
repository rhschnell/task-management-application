package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.CardList;
import commons.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardListRepository;

import java.util.List;

@RestController
@RequestMapping(Route.CARD_LIST)
public class CardListController {
    private CardListRepository cardLists;

    /**
     * Creates a new BoardController
     * @param cardLists Instance of board repository
     */
    public CardListController (CardListRepository cardLists)
    {
        this.cardLists = cardLists;
    }

    /**
     * Adds a board to the database
     * @param cardList The board to add
     * @return The added board
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<CardList> add(@RequestBody CardList cardList)
    {
        return ResponseEntity.ok(cardLists.save(cardList));
    }

    /**
     * Gets all boards from the database
     * @return List of all boards in the database
     */
    @GetMapping(path = {"", "/"})
    public List<CardList> getAll()
    {
        return cardLists.findAll();
    }

    /**
     * Gets a specific board from the database
     * @param id ID of the board
     * @return If the board was not found, status code 400, else status code 200 and data in the body
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id)
    {
        if(id < 0 || !cardLists.existsById(id))
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardLists.findById(id).get());
    }

    /**
     * Deletes a board from the database
     * @param id The id of the board to delete
     * @return The deleted board if it was found, otherwise a bad request response
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") long id) throws JsonProcessingException
    {
        if (id < 0)
        {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<CardList> response = getById(id);

        if (response.getStatusCode() != HttpStatus.OK)
        {
            return response;
        }

        CardList deleted = response.getBody();
        cardLists.delete(deleted);

        if (deleted == null) throw new IllegalStateException("Why is this null?");
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(deleted));
    }
}
