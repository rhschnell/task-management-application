package server.features.cardlists;

import commons.CardList;
import commons.Route;
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
    private Boolean testing = false;

    /**
     * Disables websockets for testing
     * @param testing
     */
    public void setTesting(Boolean testing) {
        this.testing = testing;
    }

    /**
     * Creates a new CardListController
     *
     * @param service Instance of card list repository
     * @param sender The sender
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
            if(!testing)
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
     * Deletes the card with the specified id from the cardList that contains that
     * card
     * @param id the card's id
     * @return the response
     */
    @DeleteMapping("/deleteCard/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> deleteCardById(@PathVariable("id") long id) {
        try {
            CardList list =null;
            int place = -1;
            for(int j=0;j<service.getRepo().findAll().size();j++)
                for(int i=0;i<service.getRepo().findAll().get(j).getCards().size();i++)
                {
                    if(service.getRepo().findAll().get(j).getCards().get(i).getId()==id) {
                        {
                            list = service.getRepo().findAll().get(j);
                            place = i;
                            break;
                        }

                    }
                }
            if(list != null) {
                list.removeCard(place);
                service.getRepo().save(list);
                if(!testing)
                        sender.convertAndSend("/topic/lists/" + list.getId(), list);
            }
            return ResponseEntity.ok().build();
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