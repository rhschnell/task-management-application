package server.features.cardlists;

import commons.Card;
import commons.CardList;
import commons.Tag;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import server.features.RepositoryService;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CardListService implements RepositoryService<CardList, Long> {
    private final CardListRepository repo;

    public CardListService(CardListRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts given card list into repository
     *
     * @param cardList the card list to be inserted
     */
    @Override
    public CardList insert(CardList cardList) {
        if (cardList == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(cardList);
    }

    /**
     * Deletes card list with given ID from repository
     * @param id id of card list to be deleted
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
     * Finds and returns card list with given ID from repository
     * @param id id of entity to be found and returned
     * @return card list corresponding to given id
     */
    @Override
    public CardList getByID(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        return repo.getById(id);
    }
    public CardListRepository getRepo() {
        return repo;
    }

    /**
     * Returns all card lists from repository
     * @return list of all card lists from repository
     */
    @Override
    public List<CardList> getAll() {
        return repo.findAll();
    }
}
