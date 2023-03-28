package commons;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String listTitle;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")

    @OrderBy("priority ASC")
    private List<Card> cards = new ArrayList<>();
    /**
     * Constructor for Tests
     * @param listTitle Title of the list
     * @param cards ArrayList of cards in the list
     */
    public CardList(String listTitle, ArrayList<Card> cards) {
        this.listTitle = listTitle;
        this.cards = cards;
    }

    /**
     * Adds a card to the end of the list.
     * @param card Card to be added
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Adds a card to an arbitrary position in the list (zero-indexed)
     * @param card Card to be added
     * @param index The index the cards needs to end up at
     */
    public void addCard(Card card, int index) {
        this.cards.add(index, card);
        int i=index;
        long swap;
        while((i+1)<cards.size() && cards.get(i).getPriority()>cards.get(i+1).getPriority())
        {
            swap =  cards.get(i).getPriority();
            cards.get(i).setPriority(cards.get(i+1).getPriority());
            cards.get(i+1).setPriority(swap);
            i++;
        }
        while((i-1)>0 && cards.get(i).getPriority()<cards.get(i-1).getPriority())
        {
            swap =  cards.get(i).getPriority();
            cards.get(i).setPriority(cards.get(i-1).getPriority());
            cards.get(i+1).setPriority(swap);
            i--;
        }
    }

    /**
     * Removes cards based on object reference
     * @param card Card to be removed
     * @return The card if successfully deleted, else null
     */
    public Card removeCard(Card card) {
        if (!this.cards.contains(card)) {
            return null;
        }
        this.cards.remove(card);
        return card;
    }

    /**
     * Removes cards based on position in zero-indexed list
     * @param index Index of the card to be removed
     * @return The card if successfully deleted, else null
     */
    public Card removeCard(int index) {
        if (index >= this.cards.size()) {
            return null;
        }
        return this.cards.remove(index);
    }

    /**
     * Gets card by index
     * @param index Index of the card to get
     * @return The card at the index
     */
    public Card getCard(int index) {
        return this.cards.get(index);
    }

    /**
     * Moves card to specified index
     * @param card Card to move
     * @param index Index to move to
     */
    public void moveCard(Card card, int index) {
        removeCard(card);
        addCard(card, index);
    }

    /**
     * Moves card from from-index to to-index
     * @param from Index to move from
     * @param to Index to move to
     */
    public void moveCard(int from, int to) {
        Card c = getCard(from);
        moveCard(c, to);
    }
}
