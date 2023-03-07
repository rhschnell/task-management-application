package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tag {

    private String name;

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
     * @param name The name of the tag
     */

    public Tag(String name){
        this.name = name;
    }


    /**
     * Returns the name of the tag
     * @return The tag's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag
     * @param name The new tag name
     */
    public void setName(String name) {
        this.name = name;
    }
}
