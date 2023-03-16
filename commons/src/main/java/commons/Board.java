package commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private String title;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<CardList> cardLists; // Use a list here to make the annotation work

    @Id
    private String key;

    /**
    Constructor for the class Board
     * @param title Title of the board
     * @param key Key of the board
     * @param cardLists The lists of cards in this board
     */
    public Board(String title, String key, ArrayList<CardList> cardLists){
        this.title = title;
        this.key = key;
        this.cardLists = cardLists;
    }

    /**
     * Adds a new clean card-list to the board
     */
    public void addList(){
        CardList list = new CardList();
        this.cardLists.add(list);
    }

    /**
     * Adds a new card-list to the board
     * @param list Card-list to be added to the board
     */
    public void addList(CardList list){
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
     * Removes the card-list with the given index from the board
     * @param index Index of the card-list to be removed
     */
    public void removeListByIndex(int index){
        this.cardLists.remove(index);
    }

    /**
     * Method to get the amount of card-lists on the board
     * @return The amount of card-lists that are on the board
     */
    public int getAmountList(){
        return this.cardLists.size();
    }

}
