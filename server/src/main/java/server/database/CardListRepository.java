package server.database;

import commons.CardList;
import org.springframework.stereotype.Repository;

@Repository
public interface CardListRepository extends GetByIDRepo<CardList, Long> {
}
