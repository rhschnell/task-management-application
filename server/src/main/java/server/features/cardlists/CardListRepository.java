package server.cardlists;

import commons.CardList;
import org.springframework.stereotype.Repository;
import server.GetByIDRepo;

@Repository
public interface CardListRepository extends GetByIDRepo<CardList, Long> {
}
