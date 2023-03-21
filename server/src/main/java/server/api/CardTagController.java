package server.api;

import commons.Card;
import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
import server.database.TagRepository;

@RestController
@RequestMapping("/api/tagToCard")
public class CardTagController {
    private CardRepository cards;
    private TagRepository tags;
    public CardTagController(CardRepository cards, TagRepository tags) {
        this.cards = cards;
        this.tags = tags;
    }
    @PostMapping(path = {"","/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag, @RequestParam long cardId) {
        Tag saved = tags.save(tag);
        Card parentCard = cards.findById(cardId).get();
        parentCard.addTag(tag);
        cards.save(parentCard);
        return ResponseEntity.ok(saved);
    }
}
