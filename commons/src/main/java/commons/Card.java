package commons;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
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
    public String getTitle()
    {
        return this.title;
    }
    public String getDescription()
    {
        return this.description;
    }

    /**
     *
     * @param o
     * @return if the Object is of type Card end equal to the Card
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        //Line was too long for checkstyle
        Boolean first;
        Boolean second;
        first =id==card.id && Objects.equals(title, card.title) && Objects.equals(description, card.description);
        second = Objects.equals(backgroundColour, card.backgroundColour) && Objects.equals(tags, card.tags) ;
        return first && second && Objects.equals(subTasks, card.subTasks);
    }

    /**
     * Return the hashcode of the object
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, description, backgroundColour, tags, subTasks, id);
    }
}
