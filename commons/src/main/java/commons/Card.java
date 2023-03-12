package commons;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
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
        this.tags.add(newTag);
    }

    /**
     * Add new sub task to the list
     *
     * @param newTask New sub task
     */
    public void addSubTask(Task newTask) {
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
     * @param tag Name of the tag to be deleted
     */
    public void deleteTag(String tag) {
        if (this.tags.contains(tag)) {
            this.tags.remove(tag);
        }
    }

    /**
     * Delete sub task by index
     *
     * @param index Index of the sub task to be deleted
     */
    public void deleteSubTask(int index) {
        if (this.subTasks.size() > index) {
            this.subTasks.remove(index);
        }
    }

    /**
     * Delete sub task by object
     *
     * @param task Object to be deleted from list
     */
    public void deleteSubTask(Task task) {
        if (this.subTasks.contains(task)) {
            this.subTasks.remove(task);
        }
    }
}
