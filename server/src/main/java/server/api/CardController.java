package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Card;
import commons.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping(Route.CARD)
public class CardController {
    private CardRepository cards;

    /**
     * Creates a new CardController
     *
     * @param cards Instance of card repository. Injected with SpringBoot
     */
    public CardController(CardRepository cards) {
        this.cards = cards;
    }

    /**
     * Adds a card to the database
     *
     * @param card The card to add
     * @return The added card
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Card> add(@RequestBody Card card) {
        return ResponseEntity.ok(cards.save(card));
    }


    /**
     * Gets all cards from the database
     *
     * @return All cards in the database
     */
    @GetMapping(path = {"", "/"})
    public List<Card> findAll() {
        return cards.findAll();
    }


    /**
     * Gets a specific card from the database
     *
     * @param id The id of the card
     * @return HTTP response with code 200 (ok) if the card was found, with the card data in the
     * body. If the card was not found, contains status code 400 (bad request) and no body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !cards.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cards.getById(id));
    }


    /**
     * Deletes a card from the database
     *
     * @param id The id of the card to delete
     * @return The deleted card if it existed, otherwise a bad request response
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") long id) throws JsonProcessingException {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }
        ResponseEntity<Card> response = getById(id);

        if (response.getStatusCode() != HttpStatus.OK) {
            // Card does not exist; bad request
            return response;
        }

        Card deleted = response.getBody();

        // Delete the card from the database and return it
        cards.delete(deleted);

        if (deleted == null) throw new IllegalStateException("Why is this null?");
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(deleted));
    }
}
