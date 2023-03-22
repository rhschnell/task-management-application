package commons;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Card implements Serializable {
    private String title;
    private String description;
    private String backgroundColour;



    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "Card_Tag",
            joinColumns = {
                @JoinColumn(name = "card_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name="tag_id",referencedColumnName = "id")
            })
    private List<Tag> tags;

    /**
     * Custom constructor for all parameters without ID (auto-generated)
     * Creates a new Card
     *
     * @param title Title of the card
     * @param description Description of the card
     * @param backgroundColour Background colour of the card
     * @param tags Tags associated with this card
     * @param subTasks Subtasks for this card
     * @param id id
     */
    public Card(String title, String description, String backgroundColour, List<Tag> tags,
                List<Task> subTasks, long id){
        this.title =title;
        this.description = description;
        this.backgroundColour = backgroundColour;
        this.tags = tags;
        this.subTasks = subTasks;
        this.id = id;
    }

    /**
     * Custom constructor for all parameters without ID (auto-generated)
     * Creates a new Card
     *
     * @param title Title of the card
     * @param description Description of the card
     * @param backgroundColour Background colour of the card
     * @param tags Tags associated with this card
     * @param subTasks Subtasks for this card
     */
    public Card(String title, String description, String backgroundColour, List<Tag> tags,
                List<Task> subTasks){
        this.title =title;
        this.description = description;
        this.backgroundColour = backgroundColour;
        this.tags = tags;
        this.subTasks = subTasks;
    }

    /**
     * Add new tag to tag list
     *
     * @param newTag New tag
     */
    public void addTag(Tag newTag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(newTag);
    }

    /**
     * Add new subtask to the list
     *
     * @param newTask New subtask
     */
    public void addSubTask(Task newTask) {
        if (this.subTasks == null) {
            this.subTasks = new ArrayList<>();
        }
        this.subTasks.add(newTask);
    }

    /**
     * Delete tag by index
     *
     * @param index Index of the tag to be deleted
     */
    public void deleteTag(int index) {
        if (this.tags.size() > index) {
            this.tags.remove(index);
        }
    }

    /**
     * Delete tag by name
     *
     * @param tagName Name of the tag to be deleted
     */
    public void deleteTag(String tagName) {
        // This approach avoids concurrent modifications
        Tag toRemove = null;
        for (Tag tag : tags) {
            if (tag.getName().equals(tagName)) {
                toRemove = tag;
                break;
            }
        }
        tags.remove(toRemove);
    }

    /**
     * Delete subtask by index
     *
     * @param index Index of the subtask to be deleted
     */
    public void deleteSubTask(int index) {
        if (this.subTasks.size() > index) {
            this.subTasks.remove(index);
        }
    }

    /**
     * Delete subtask by object
     *
     * @param task Task to be deleted from list
     */
    public void deleteSubTask(Task task) {
        this.subTasks.remove(task);
    }
}
