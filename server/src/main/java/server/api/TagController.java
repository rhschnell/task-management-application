package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Card;
import commons.Route;
import commons.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TagRepository;

import java.util.List;

@RestController
@RequestMapping(Route.TAG)
public class TagController {
    private TagRepository tags;

    /**
     * Creates a new CardController
     *
     * @param tags Instance of card repository. Injected with SpringBoot
     */
    public TagController(TagRepository tags) {
        this.tags = tags;
    }

    /**
     * Adds a card to the database
     *
     * @param tag The card to add
     * @return The added card
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        return ResponseEntity.ok(tags.save(tag));
    }


    /**
     * Gets all tags from the database
     *
     * @return All tags in the database
     */
    @GetMapping(path = {"", "/"})
    public List<Tag> findAll() {
        return tags.findAll();
    }


    /**
     * Gets a specific tag from the database
     *
     * @param id The id of the tag
     * @return HTTP response with code 200 (ok) if the tag was found, with the card data in the
     * body. If the card was not found, contains status code 400 (bad request) and no body.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id < 0 || !tags.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tags.getById(id));
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
        ResponseEntity<Tag> response = getById(id);

        if (response.getStatusCode() != HttpStatus.OK) {
            // Card does not exist; bad request
            return response;
        }

        Tag deleted = response.getBody();

        // Delete the card from the database and return it
        tags.delete(deleted);

        if (deleted == null) throw new IllegalStateException("Why is this null?");
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(deleted));
    }
}
