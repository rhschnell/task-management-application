package server.features.tags;

import commons.Route;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.TAG)
public class TagController {
    private final TagService service;

    /**
     * Creates a new TagController
     * @param service Instance of tag repository
     */
    public TagController(TagService service) {
        this.service = service;
    }

    /**
     * Adds a tag to the database
     * @param tag The tag list to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody Tag tag) {
        try {
            service.insert(tag);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a tag from the database
     * @param id The id of the tag to delete
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
     * Gets a specific tag from the database
     * @param id The id of the tag
     * @return ResponseEntity with code 200, containing the requested tag, if successful or occurring error code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        try {
            Tag returnTag = service.getByID(id);
            return ResponseEntity.ok(returnTag);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Gets all tags from the database
     * @return ResponseEntity containing a list of all tags
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Tag>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}