package server.database;

import commons.Card;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends GetByIDRepo<Card, Long> {
}
