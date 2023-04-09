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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "Card_Preset",
            joinColumns = {
                @JoinColumn(name = "card_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "preset_id", referencedColumnName = "id")
            })
    private List<CardColorPreset> presets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    @OrderBy("priority ASC")
    private List<Task> subTasks;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long priority;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "Card_Tag",
            joinColumns = {
                @JoinColumn(name = "card_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "tag_id", referencedColumnName = "id")
            })
    private List<Tag> tags;

    /**
     * Custom constructor for all parameters without ID (auto-generated)
     * Creates a new Card
     *
     * @param title            Title of the card
     * @param description      Description of the card
     * @param tags             Tags associated with this card
     * @param subTasks         Subtasks for this card
     * @param id               id
     */
    public Card(String title, String description, List<Tag> tags,
                List<Task> subTasks, long id) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.subTasks = subTasks;
        this.id = id;
    }

    /**
     * Custom constructor for all parameters without ID (auto-generated)
     * Creates a new Card
     *
     * @param title            Title of the card
     * @param description      Description of the card
     * @param tags             Tags associated with this card
     * @param subTasks         Subtasks for this card
     * @param presets          Presets associated with this card
     */
    public Card(String title, String description, List<Tag> tags,
                List<Task> subTasks, List<CardColorPreset> presets) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.subTasks = subTasks;
        this.presets = presets;
        if(this.presets == null) {
            this.presets = new ArrayList<>();
        }
    }

    /**
     * @param title            Title of the card
     * @param description      Description of the card
     * @param tags             Tags associated with this card
     * @param subTasks         Subtasks for this card
     * @param priority         The priority of the card
     */

    public Card(String title, String description, List<Tag> tags,
                List<Task> subTasks, Long priority) {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.subTasks = subTasks;
        this.priority = priority;
    }

    /**
     * Create a card with just a title
     *
     * @param title Title of the card
     */
    public Card(String title) {
        this.title = title;
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
        newTask.setPriority(this.subTasks.size() + 1);
        this.subTasks.add(newTask);
    }

    /**
     * Add new subtask by index
     *
     * @param index   The index at which the new subtask should appear
     * @param newTask The subtask to add
     */
    public void addSubTask(int index, Task newTask) {
        if (this.subTasks == null) {
            this.subTasks = new ArrayList<>();
        }
        if (index > subTasks.size()) index = (subTasks.size());
        this.subTasks.add(index, newTask);

        // Update priorities (naive)
        for (int i = 0; i < this.subTasks.size(); i++) {
            this.subTasks.get(i).setPriority(i + 1);
        }
    }

    /**
     * Removes the tag from the card
     * @param tag
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
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

    /**
     * Check if this card has a description
     *
     * @return Whether this card has a description
     */
    public boolean hasDescription() {
        return this.description != null && !this.description.isEmpty();
    }

    /**
     * Deletes a tag from this card
     * @param tag The tag to delete
     */
    public void deleteTag(Tag tag) {
        this.tags.remove(tag);
    }

    /**
     * Adds a new preset to the card's preset list
     * @param preset The preset to be added
     */
    public void setPreset(CardColorPreset preset){
        this.presets.add(preset);
    }
}
