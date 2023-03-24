package server.cards;

import commons.Card;
import org.springframework.stereotype.Repository;
import server.GetByIDRepo;

@Repository
public interface CardRepository extends GetByIDRepo<Card, Long> {
}
