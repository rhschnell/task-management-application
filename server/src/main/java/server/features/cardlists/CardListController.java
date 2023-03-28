package server.features.cardlists;

import commons.Card;
import commons.CardList;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD_LIST)
public class CardListController {
    private final CardListService service;

    /**
     * Creates a new CardListController
     *
     * @param service Instance of card list repository
     */
    public CardListController(CardListService service) {
        this.service = service;
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
            service.insert(cardList);
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

    @GetMapping("/getListLength/{id}")
    public ResponseEntity<Integer> getListLength(@PathVariable("id") long id) {
        try {
            Integer returnCardList = service.getListLength(id);
            return ResponseEntity.ok(returnCardList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/removeFromCardList/")
    public ResponseEntity<Card> removeFromCardList(@RequestBody Card card) {
       return ResponseEntity.ok(service.removeFromCardList(card));
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