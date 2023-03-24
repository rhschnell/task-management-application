package server.features.cards;

import commons.Card;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;

@Repository
public interface CardRepository extends CustomRepository<Card, Long> {
}
