package server.features.cardlists;

import commons.Card;
import commons.CardList;
import org.springframework.stereotype.Service;
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
    public void insert(CardList cardList) {
        if (cardList == null) {
            throw new IllegalArgumentException();
        }
        repo.save(cardList);
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

    public Card removeFromCardList(Card card) {
        for(int i=0;i<repo.findAll().size();i++)
        {
            if(repo.findAll().get(i).getCards().contains(card)) {
                CardList repoList = repo.findAll().get(i);
                repoList.removeCard(card);
                repo.save(repoList);
            }}

        return card;
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
