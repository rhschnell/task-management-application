package commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    @Id
    private String key;

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "key")
    private List<CardList> cardLists; // Use a list here to make the annotation work

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "key")
    private List<Tag> tagList; // Use a list here to make the annotation work


    /**
     * Constructor for board class
     * @param key key
     * @param title title
     * @param cardLists null
     */
    public Board(String key, String title, List<CardList> cardLists, List<Tag> tagList) {
        this.key = key;
        this.title = title;
        this.cardLists = cardLists;
        if (cardLists == null) {
            this.cardLists = new ArrayList<>();
        }
        this.tagList = tagList;
        if(tagList == null){
            this.tagList = new ArrayList<>();
        }
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
     * Getter
     * @return amount of lists
     */
    public int getAmountList() {
        return this.cardLists.size();
    }

    public void addTag(Tag tag) {
        tagList.add(tag);
    }
}
