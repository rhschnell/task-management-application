package server.cardlists;

import commons.CardList;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD_LIST)
public class CardListController {
    private CardListRepository cardLists;

    /**
     * Creates a new CardListController
     *
     * @param cardLists Instance of cardList repository
     */
    public CardListController(CardListRepository cardLists) {
        this.cardLists = cardLists;
    }

    /**
     * Adds a cardList to the database
     *
     * @param cardList The cardList to add
     * @return The added cardList
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<CardList> add(@RequestBody CardList cardList) {
        return ResponseEntity.ok(cardLists.save(cardList));
    }

    /**
     * Gets all cardList from the database
     *
     * @return List of all cardList in the database
     */
    @GetMapping(path = {"", "/"})
    public List<CardList> findAll() {
        return cardLists.findAll();
    }

    /**
     * Gets a specific cardList from the database
     *
     * @param id ID of the cardList
     * @return If the cardList was not found, status code 400, else status code 200 and data in the body
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardList> getById(@PathVariable("id") long id) {
        if (id < 0 || !cardLists.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cardLists.getById(id));
    }

    /**
     * Deletes a card list from the database
     *
     * @param id The id of the card list to delete
     * @return A successful response message if the card list was found, otherwise a 404 not found
     * response
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (!cardLists.existsById(id)) return ResponseEntity.notFound().build();
        cardLists.deleteById(id);
        return ResponseEntity.ok(String.format("Successfully deleted the card list with id %d",
                id));
    }
}
