package server.features.presets;

import commons.CardColorPreset;
import commons.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.CARD_COLOR_PRESET)
public class PresetController {
    private final PresetService service;

    public PresetController(PresetService service){
        this.service = service;
    }

    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody CardColorPreset preset){
        try {
            service.insert(preset);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

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

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<CardColorPreset>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

}
