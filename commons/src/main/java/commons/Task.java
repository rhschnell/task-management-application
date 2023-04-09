package commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean completed;
    private String title;
    private long priority;

    /**
     * Constructor for Task class with parameters
     * @param status The status of the task (whether it is done or not,
     *               false = not done, true = done)
     * @param title The title of the task
     */
    public Task(boolean status, String title)
    {
        this.completed = status;
        this.title = title;
    }
}
