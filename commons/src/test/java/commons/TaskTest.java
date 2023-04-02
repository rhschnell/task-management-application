package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    public void before()
    {
        task = new Task(0, false, "Task", 0);
    }

    @Test
    void constructorWithoutId()
    {
        task = new Task(false, "Task");
        assertNotNull(task);
    }

    @Test
    void noArgsConstructor()
    {
        task = new Task();
        assertNotNull(task);
    }

    @Test
    void getId() {
        assertEquals(0, task.getId());
    }

    @Test
    void isCompleted() {
        assertFalse(task.isCompleted());
    }

    @Test
    void getTitle() {
        assertEquals("Task", task.getTitle());
    }

    @Test
    void setId() {
        task.setId(1);
        assertEquals(1, task.getId());
    }

    @Test
    void setCompleted() {
        task.setCompleted(true);
        assertTrue(task.isCompleted());
    }

    @Test
    void setTitle() {
        task.setTitle("Test");
        assertEquals("Test", task.getTitle());
    }

    @Test
    void getPriority() {
        assertEquals(0, task.getPriority());
    }

    @Test
    void setPriority() {
        task.setPriority(12);
        assertEquals(12, task.getPriority());
    }
}