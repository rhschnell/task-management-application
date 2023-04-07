package server.features.cards;

import commons.Card;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CardService implements RepositoryService<Card, Long> {
    private final CardRepository repo;

    public CardService(CardRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts given card into repository
     * @param card the card to be inserted
     */
    @Override
    public Card insert(Card card) {
        if (card == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(card);
    }

    /**
     * Deletes card with given ID from repository
     * @param id id of card to be deleted
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
     * Finds and returns card with given ID from repository
     * @param id id of entity to be found and returned
     * @return card corresponding to given id
     */
    @Override
    public Card getByID(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        return repo.getById(id);
    }

    /**
     * Returns all cards from repository
     * @return list of all cards from repository
     */
    @Override
    public List<Card> getAll() {
        return repo.findAll();
    }
}