package server.features.presets;

import commons.CardColorPreset;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PresetService implements RepositoryService<CardColorPreset, Long> {
    private final PresetRepository repo;

    public PresetService(PresetRepository repo){
        this.repo = repo;
    }

    @Override
    public void insert(CardColorPreset preset){
        if (preset == null) {
            throw new IllegalArgumentException();
        }
        repo.save(preset);
    }

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

    @Override
    public List<CardColorPreset> getAll(){
        return repo.findAll();
    }

}
