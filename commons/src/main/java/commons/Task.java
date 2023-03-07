package commons;

public class Task {
    private boolean status;
    private String title;

    public Task()
    {
        this.status = false;
        this.title = "";
    }

    public Task(boolean status, String title)
    {
        this.status = status;
        this.title = title;
    }

    public void setDone(boolean status)
    {
        this.status = status;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean getStatus()
    {
        return this.status;
    }

    public String getTitle()
    {
        return this.title;
    }

}
