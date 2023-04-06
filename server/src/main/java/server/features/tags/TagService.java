package server.features.tags;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TagService implements RepositoryService<Tag, Long> {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts given entity into repository
     *
     * @param tag entity to be inserted
     */
    @Override
    public void insert(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException();
        }
        repo.save(tag);
    }

    @Override
    public void delete(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        repo.deleteById(id);
    }

    @Override
    public Tag getByID(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        return repo.getById(id);
    }

    @Override
    public List<Tag> getAll() {
        return repo.findAll();
    }
}
