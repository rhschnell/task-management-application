package server.features.cardlists;

import commons.CardList;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;

@Repository
public interface CardListRepository extends CustomRepository<CardList, Long> {
}
