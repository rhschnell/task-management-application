package commons;

import java.util.ArrayList;

public class CardList {
    private String listTitle;
    private ArrayList<Card> cards;

    /**
     * Empty constructor for Spring Boot
     */
    public CardList() {

    }

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

    /**
     * Setter for the title of the list
     * @param listTitle New title of the list
     */
    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    /**
     * Same as setListTitle(String) but with different name for ease of use.
     * @param listTitle New title of the list
     */
    public void rename(String listTitle) {
        setListTitle(listTitle);
    }

    /**
     * Setter for the cards PQ of the board. Mainly because Spring Boot needs it.
     * @param cards The new PQ of cards this list will contain.
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Getter for title of the list
     * @return The title of the list
     */
    public String getListTitle() {
        return listTitle;
    }

    /**
     * Getter for the card list
     * @return The list of cards
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
