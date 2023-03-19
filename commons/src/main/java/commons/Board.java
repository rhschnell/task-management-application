package commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long key;

    private String title;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<CardList> cardLists; // Use a list here to make the annotation work

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

}
