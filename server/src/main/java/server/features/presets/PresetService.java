package server.features.presets;

import commons.CardColorPreset;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PresetService implements RepositoryService<CardColorPreset, Long> {
    private final PresetRepository repo;

    /**
     * Constructor for the PresetService
     * @param repo a PresetRepository instance
     */
    public PresetService(PresetRepository repo){
        this.repo = repo;
    }

    /**
     * Inserts the given entity into the repository
     * @param preset entity to be inserted
     * @return preset that is saved
     */
    @Override
    public CardColorPreset insert(CardColorPreset preset){
        if (preset == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(preset);
    }

    /**
     * Deletes a preset with given ID from repository
     * @param id id of entity to be deleted
     */
    @Override
    public void delete(Long id){
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)){
            throw new EntityNotFoundException();
        }
        repo.deleteById(id);
    }

    /**
     * Finds and returns a preset with the given ID from the repository
     * @param id id of entity to be found and returned
     * @return preset corresponding to the given id
     */
    @Override
    public CardColorPreset getByID(Long id){
        if(id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)){
            throw new EntityNotFoundException();
        }
        return repo.getById(id);
    }

    /**
     * Returns all presets from the repository
     * @return List of all presets from the repository
     */
    @Override
    public List<CardColorPreset> getAll(){
        return repo.findAll();
    }

}
