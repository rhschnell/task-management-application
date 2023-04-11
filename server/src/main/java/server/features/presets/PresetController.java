package server.features.presets;

import commons.CardColorPreset;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD_COLOR_PRESET)
public class PresetController {
    private final PresetService service;

    /**
     * Creates a new PresetController
     *
     * @param service               Instance of preset repository
     * @param simpMessagingTemplate
     */
    public PresetController(PresetService service, SimpMessagingTemplate simpMessagingTemplate){
        this.service = service;
    }

    /**
     * Adds a preset to the database
     * @param preset The preset list to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<CardColorPreset> insert(@RequestBody CardColorPreset preset){
        try {
            return ResponseEntity.ok(service.insert(preset));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a preset from the database
     * @param id The id of the preset to delete
     * @return ResponseEntity with code 200 if successful or occurring error code
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
     * Gets a specific preset from the database
     * @param id The id of the preset
     * @return ResponseEntity with code 200, containing the requested tag, if successful or occurring error code
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardColorPreset> getById(@PathVariable("id") long id){
        try {
            CardColorPreset preset = service.getByID(id);
            return ResponseEntity.ok(preset);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets all the presets from the database
     * @return ResponseEntity containing a list of all presets
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<CardColorPreset>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
