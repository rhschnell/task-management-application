package commons;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card implements Serializable {
    private String title;
    private String description;
    private String backgroundColour;

    @ManyToMany
    private List<Tag> tags;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


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
