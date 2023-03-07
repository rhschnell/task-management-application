package commons;

import java.util.ArrayList;

public class Card {
    private String title;
    private String description;
    private String backgorundColour;
    private ArrayList<String> tags;
    private ArrayList<String> tasks;

    public Card()
    {
        title = "New Card";
        description = "Empty description";
        backgorundColour = "White"; // we can change the default later
        tags = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    public Card(String title, String description, String backgorundColour, ArrayList<String> tags, ArrayList<String> tasks)
    {
        this.title = title;
        this.description = description;
        this.backgorundColour = backgorundColour;
        this.tags = tags;
        this.tasks = tasks;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setBackgorundColour(String backgorundColour)
    {
        this.backgorundColour = backgorundColour;
    }

    public void setTags(ArrayList<String> tags)
    {
        this.tags = tags;
    }

    public void setTasks(ArrayList<String> tasks)
    {
        this.tasks = tasks;
    }

    public void addTags(String newTag)
    {
        this.tags.add(newTag);
    }

    public void addTask(String newTask)
    {
        this.tasks.add(newTask);
    }

    public void deleteTag(int index)
    {
        if(this.tags.size() > index)
        {
            this.tags.remove(index);
        }
    }

    public void deleteTag(String tag)
    {
        if(this.tags.contains(tag))
        {
            this.tags.remove(tag);
        }
    }

    public void deleteTask(int index)
    {
        if(this.tasks.size() > index)
        {
            this.tasks.remove(index);
        }
    }

    public void deleteTask(String task)
    {
        if(this.tasks.contains(task))
        {
            this.tasks.remove(task);
        }
    }
}
