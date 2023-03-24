package server.cards;

import commons.Card;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    @Transactional
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
     * body. If the card was not found, contains status code 404 (not found) and no body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !cards.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cards.getById(id));
    }


    /**
     * Deletes a card from the database
     *
     * @param id The id of the card to delete
     * @return A successful response message if the card was found, otherwise a 404 not found
     * response
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!cards.existsById(id)) return ResponseEntity.notFound().build();
        cards.deleteById(id);
        return ResponseEntity.ok(String.format("Successfully deleted the card with id %d", id));
    }
}
