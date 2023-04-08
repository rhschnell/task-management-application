package server.features.cardlists;

import commons.Card;
import commons.CardList;
import commons.Route;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD_LIST)
public class CardListController {
    private final CardListService service;
    private final SimpMessagingTemplate sender;

    /**
     * Creates a new CardListController
     *
     * @param service Instance of card list repository
     */
    public CardListController(CardListService service, SimpMessagingTemplate sender) {
        this.service = service;
        this.sender=sender;
    }


    /**
     * Adds a card list to the database
     *
     * @param cardList The card list to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody CardList cardList) {
        try {
            CardList inserted = service.insert(cardList);
            sender.convertAndSend("/topic/lists/"+cardList.getId(),inserted);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a card list from the database
     *
     * @param id The id of the card list to delete
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
     * Gets a specific card list from the database
     *
     * @param id The id of the board
     * @return ResponseEntity with code 200, containing the requested card list,
     * if successful or occurring error code
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        try {
            CardList returnCardList = service.getByID(id);
            return ResponseEntity.ok(returnCardList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     *
     * @param card The card that needs to be removed using the delete method from the object
     *             so the priority is preserved.
     * @return the card that has been deleted
     */
    @PostMapping("/removeFromCardList/")
    public ResponseEntity<Card> removeFromCardList(@RequestBody Card card) {
        try {
           // Ret
          //  System.out.println("test");
            //CardList returned = service.removeFromCardList(card);
            System.out.println("entered");
            CardList returned = service.removeFromCardList(card);
            returned.removeCard(card);
            System.out.println(returned);
            System.out.println("exited");
            insert(returned);
            //sender.convertAndSend("/topic/lists/"+returned.getId(),returned);
            return ResponseEntity.ok(new Card());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Gets all card lists from the database
     *
     * @return ResponseEntity containing a list of all card lists
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<CardList>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}