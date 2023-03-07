package commons;

public class Task {
    private boolean status;
    private String title;

    /**
     * Basic constructor for Task class
     */
    public Task()
    {
        this.status = false;
        this.title = "";
    }

    /**
     * Constructor for Task class with parameters
     * @param status The status of the task (whether it is done or not, false = not done, true = done)
     * @param title The title of the task
     */
    public Task(boolean status, String title)
    {
        this.status = status;
        this.title = title;
    }

    /**
     * Setter for the status
     * @param status Updated status
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /**
     * Setter for the title
     * @param title New title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Getter for the status
     * @return Status of the task
     */
    public boolean getStatus()
    {
        return this.status;
    }

    /**
     * Getter for the title
     * @return Title of the task
     */
    public String getTitle()
    {
        return this.title;
    }

}
