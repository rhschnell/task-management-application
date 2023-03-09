package commons;

import java.util.ArrayList;

public class Card {
    private String title;
    private String description;
    private String backgroundColour;
    private ArrayList<String> tags;
    private ArrayList<Task> subTasks;

    /**
     * Basic constructor for Card class, does not require any parameters
     */
    public Card()
    {
        title = "New Card";
        description = "Empty description";
        backgroundColour = "White"; // we can change the default later
        tags = new ArrayList<>();
        subTasks = new ArrayList<>();
    }

    /**
     * Constructor for Card class with parameters
     * @param title Title parameter
     * @param description Description of the task
     * @param backgroundColour Background colour of the Card
     * @param tags Tags assigned to the Card
     * @param subTasks ArrayList of subtasks to give more context about a card
     */
    public Card(String title, String description, String backgroundColour, ArrayList<String> tags, ArrayList<Task> subTasks)
    {
        this.title = title;
        this.description = description;
        this.backgroundColour = backgroundColour;
        this.tags = tags;
        this.subTasks = subTasks;
    }

    /**
     * Setter for title parameter
     * @param title New title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Setter for description parameter
     * @param description New description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Setter for background colour
     * @param backgroundColour New background colour
     */
    public void setBackgroundColour(String backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Setter for tags ArrayList
     * @param tags New tags list
     */
    public void setTags(ArrayList<String> tags)
    {
        this.tags = tags;
    }

    /**
     * Setter for tasks list
     * @param tasks New tasks list
     */
    public void setSubTasks(ArrayList<Task> tasks)
    {
        this.subTasks = tasks;
    }

    /**
     * Add new tag to tag list
     * @param newTag New tag
     */
    public void addTags(String newTag)
    {
        this.tags.add(newTag);
    }

    /**
     * Add new sub task to the list
     * @param newTask New sub task
     */
    public void addSubTask(Task newTask)
    {
        this.subTasks.add(newTask);
    }

    /**
     * Delete tag by index
     * @param index Index of the tag to be deleted
     */
    public void deleteTag(int index)
    {
        if(this.tags.size() > index)
        {
            this.tags.remove(index);
        }
    }

    /**
     * Delete tag by name
     * @param tag Name of the tag to be deleted
     */
    public void deleteTag(String tag)
    {
        if(this.tags.contains(tag))
        {
            this.tags.remove(tag);
        }
    }

    /**
     * Delete sub task by index
     * @param index Index of the sub task to be deleted
     */
    public void deleteSubTask(int index)
    {
        if(this.subTasks.size() > index)
        {
            this.subTasks.remove(index);
        }
    }

    /**
     * Delete sub task by object
     * @param task Object to be deleted from list
     */
    public void deleteSubTask(Task task)
    {
        if(this.subTasks.contains(task))
        {
            this.subTasks.remove(task);
        }
    }
}
