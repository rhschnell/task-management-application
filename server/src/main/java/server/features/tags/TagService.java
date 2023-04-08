package server.features.tags;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TagService implements RepositoryService<Tag, Long> {
    private final TagRepository repo;

    /**
     * Constructor for TagService
     * @param repo a TagRepository instance
     */
    public TagService(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts given entity into repository
     *
     * @param tag entity to be inserted
     */
    @Override
    public Tag insert(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(tag);
    }

    /**
     * Deletes a tag with the given id
     * @param id id of entity to be deleted
     */
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

    /**
     * Retrieves a tag with the given ID
     * @param id ID of entity to be found and returned
     * @return the tag with the given ID
     */
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

    /**
     * Retrieves all the tags from the repository
     * @return a list of all existing tags
     */
    @Override
    public List<Tag> getAll() {
        return repo.findAll();
    }
}
