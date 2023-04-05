package commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    @Id
    private String key;

    private String title;
    private String password;
    private boolean secured; //could not use protected as it is a keyword in java

    private String backgroundColour = "FFFFFF";
    private String fontColour = "000000";

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
        this.password = "";
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

    public void removeTag(Tag tag) {
        tagList.remove(tag);
    }

    /**
     * Returns whether the board is protected or not
     * @return true/false
     */
    public boolean isProtected() {
        return secured;
    }

    /**
     * Setter for protected true/false
     * @param secured state of protection
     */
    public void setProtected(boolean secured) {
        this.secured = secured;
    }

    /**
     * Setter for password
     * @param password the new password
     */
    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password) || "".equals(this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(key, board.key) &&
                Objects.equals(title, board.title) &&
                Objects.equals(password, board.password) &&
                Objects.equals(backgroundColour, board.backgroundColour) &&
                Objects.equals(fontColour, board.fontColour) &&
                Objects.equals(cardLists, board.cardLists) &&
                Objects.equals(tagList, board.tagList);
    }
}
