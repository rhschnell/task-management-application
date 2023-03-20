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
     * Creates a new CardListController
     * @param cardLists Instance of cardList repository
     */
    public CardListController (CardListRepository cardLists)
    {
        this.cardLists = cardLists;
    }

    /**
     * Adds a cardList to the database
     * @param cardList The cardList to add
     * @return The added cardList
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<CardList> add(@RequestBody CardList cardList)
    {
        return ResponseEntity.ok(cardLists.save(cardList));
    }

    /**
     * Gets all cardList from the database
     * @return List of all cardList in the database
     */
    @GetMapping(path = {"", "/"})
    public List<CardList> getAll()
    {
        return cardLists.findAll();
    }

    /**
     * Gets a specific cardList from the database
     * @param id ID of the cardList
     * @return If the cardList was not found, status code 400, else status code 200 and data in the body
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
     * Deletes a cardList from the database
     * @param id The id of the cardList to delete
     * @return The deleted cardList if it was found, otherwise a bad request response
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
