package server.features.cards;

import commons.Card;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD)
public class CardController {
    private final CardService service;
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
     * Creates a new CardController
     * @param service Instance of card repository
     * @param sender the sender
     */
    public CardController(CardService service, SimpMessagingTemplate sender) {
        this.service = service;
        this.sender = sender;
    }


    /**
     * Adds a card to the database
     * @param card The card list to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody Card card) {
        try {
            service.insert(card);
            if(!testing)
                sender.convertAndSend("/topic/cards/" + card.getId(), card);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a card from the database
     * @param id The id of the card to delete
     * @return ResponseEntity with code 200 if successful or occurring error code
     *
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets a specific card from the database
     * @param id The id of the board
     * @return ResponseEntity with code 200, containing the requested card, if successful or occurring error code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        try {
            Card returnCard = service.getByID(id);
            return ResponseEntity.ok(returnCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * Gets all cards from the database
     * @return ResponseEntity containing a list of all cards
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Card>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}