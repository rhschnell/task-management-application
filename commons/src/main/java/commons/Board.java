package commons;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
    private String title;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<CardList> cardLists; // Use a list here to make the annotation work

    @Id
    private String key;

    /**
     * Empty constructor for Spring Boot
     */
    public Board(){

    }

    /**
    Constructor for the class Board
    @param title Title of the board
    @param key Key of the board
     @param cardLists The lists of cards in this board
     */
    public Board(String title, String key, ArrayList<CardList> cardLists){
        this.title = title;
        this.key = key;
        this.cardLists = cardLists;
    }

    /**
     * Adds a new card-list to the board
     */
    public void addList(){
        CardList list = new CardList();
        this.cardLists.add(list);
    }

    /**
     * Removes a card-list from the board
     * @param list Card-list to be removed
     */
    public void removeList(CardList list){
        this.cardLists.remove(list);
    }

    /**
    * Getter for the title
    * @return The title of the board
    */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of the board
     * @param title The new title of the board
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the card-lists in the board
     * @return The arraylist containing the card-lists of the board
     */
    public List<CardList> getCardLists() {
        return cardLists;
    }

    /**
     * Setter for the lists in the board
     * @param cardLists The new lists for the board
     */
    public void setCardLists(ArrayList<CardList> cardLists) {
        this.cardLists = cardLists;
    }

    /**
     * Getter for the key of the board
     * @return The key of the board
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for the key
     * @param key The new key of the board
     */
    public void setKey(String key) {
        this.key = key;
    }
}
