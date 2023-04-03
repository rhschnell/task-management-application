package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {
    private String name;

    private String tagColor;
    private String fontColor;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToMany(cascade ={CascadeType.PERSIST,CascadeType.REMOVE},
            mappedBy = "tags", fetch = FetchType.EAGER)
    private List<Card> cards;


    /**
     * Creates a new tag
     *
     * @param name The name of the tag
     * @param tagColor The color of this tag
     */

    public Tag(String name, String tagColor) {
        this.name = name;
        this.tagColor = tagColor;
        this.cards = new ArrayList<>();
        this.fontColor="Black";
    }
    /**
     * Creates a new tag
     *
     * @param name The name of the tag
     * @param backgroundColor The color of this tag
     * @param fontColor The fot color
     */
    public Tag(String name, String backgroundColor,String fontColor) {
        this.name = name;
        this.tagColor = backgroundColor;
        this.cards = new ArrayList<>();
        this.fontColor=fontColor;
    }

    /**
     * Checks the equality against another tag
     *
     * @param o The other tag to check equality against
     * @return Whether this tag is equal to the other tag
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(name, tag.name) && Objects.equals(tagColor, tag.tagColor)&& Objects.equals(fontColor, tag.fontColor);
    }

    /**
     * Generates a hash code for this tag
     *
     * @return This tag's hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, fontColor, id,tagColor);
    }

    /**
     * Generates a human-readable format of this tag
     * @return Readable description of this tag
     */
    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", backgroundColor='" + tagColor + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", id=" + id +
                ", cards=" + cards +
                '}';
    }
}
