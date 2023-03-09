package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Tag {
    private String name;


    private String color;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Default constructor
     */
    public Tag() {
    }

    /**
     * Creates a new tag
     *
     * @param name The name of the tag
     * @param color The color of this tag
     */

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }


    /**
     * Returns the name of the tag
     *
     * @return The tag's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag
     *
     * @param name The new tag name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the color of this tag
     *
     * @return This tag's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of this tag
     *
     * @param color The new color for this tag
     */
    public void setColor(String color) {
        this.color = color;
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
        return id == tag.id && Objects.equals(name, tag.name) && Objects.equals(color, tag.color);
    }

    /**
     * Generates a hash code for this tag
     *
     * @return This tag's hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, color, id);
    }
}
