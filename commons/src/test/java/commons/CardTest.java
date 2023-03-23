package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;

    @BeforeEach
    public void before() {
        card = new Card(
                "Some card",
                "This is a card",
                "White",
                null,
                null,
                0
        );
    }

    @Test
    void constructorWithoutID() {
        card = new Card(
                "My card",
                "Fancy description",
                "White",
                null,
                null
        );
        assertNotNull(card);
    }


    @Test
    void addTag() {
        Tag tag = new Tag("Some name", "Some color");
        card.addTag(tag);
        assertEquals(List.of(tag), card.getTags());
    }

    @Test
    void addSubTask() {
        Task task = new Task(false, "Task title");
        card.addSubTask(task);
        assertEquals(List.of(task), card.getSubTasks());
    }

    @Test
    void deleteTagByName() {
        Tag tag1 = new Tag("Tag 1", "Blue");
        Tag tag2 = new Tag("Tag 2", "Black");
        card.addTag(tag1);
        card.addTag(tag2);

        assertEquals(2, card.getTags().size());

        card.deleteTag("Tag 1");
        assertEquals(1, card.getTags().size());
        assertEquals(List.of(tag2), card.getTags());
    }

    @Test
    void deleteTagByIndex(){
        Tag tag1 = new Tag("Tag 1", "Blue");
        Tag tag2 = new Tag("Tag 2", "Black");
        card.addTag(tag1);
        card.addTag(tag2);

        assertEquals(2, card.getTags().size());

        card.deleteTag(0);
        assertEquals(1, card.getTags().size());
        assertEquals(List.of(tag2), card.getTags());
    }

    @Test
    void deleteSubTask() {
        Task task1 = new Task(false, "Task 1");
        Task task2 = new Task(false, "Task 2");
        card.addSubTask(task1);
        card.addSubTask(task2);

        assertEquals(2, card.getSubTasks().size());

        card.deleteSubTask(task1);
        assertEquals(1, card.getSubTasks().size());
        assertEquals(List.of(task2), card.getSubTasks());
    }

    @Test
    void deleteSubTaskByIndex(){
        Task task1 = new Task(false, "Task 1");
        Task task2 = new Task(false, "Task 2");
        card.addSubTask(task1);
        card.addSubTask(task2);

        assertEquals(2, card.getSubTasks().size());

        card.deleteSubTask(0);
        assertEquals(1, card.getSubTasks().size());
        assertEquals(List.of(task2), card.getSubTasks());
    }


    @Test
    void getTitle() {
        assertEquals("Some card", card.getTitle());
    }

    @Test
    void getDescription() {
        assertEquals("This is a card", card.getDescription());
    }

    @Test
    void getBackgroundColour() {
        assertEquals("White", card.getBackgroundColour());
    }

    @Test
    void getTags() {
        assertNull(card.getTags());
    }

    @Test
    void getSubTasks() {
        assertNull(card.getSubTasks());
    }

    @Test
    void getId() {
        assertEquals(0, card.getId());
    }

    @Test
    void setTitle() {
        card.setTitle("New title");
        assertEquals("New title", card.getTitle());
    }

    @Test
    void setDescription() {
        card.setDescription("A new description");
        assertEquals("A new description", card.getDescription());
    }

    @Test
    void setBackgroundColour() {
        card.setBackgroundColour("Black");
        assertEquals("Black", card.getBackgroundColour());
    }

    @Test
    void setTags() {
        Tag tag = new Tag("Urgent", "Black");
        List<Tag> tags = List.of(tag);
        card.setTags(tags);
        assertEquals(tags, card.getTags());

    }

    @Test
    void setSubTasks() {
        Task task = new Task();
        List<Task> tasks = List.of(task);
        card.setSubTasks(tasks);
        assertEquals(tasks, card.getSubTasks());
    }

    @Test
    void setId() {
        card.setId(102);
        assertEquals(102, card.getId());
    }

    @Test
    void testEquals() {
        Card c1 = new Card(
                "Some card",
                "This is a card",
                "White",
                null,
                null,
                0
        );
        assertEquals(card, c1);
    }

    @Test
    void testNotEquals(){
        Card c1 = new Card(
                "Some other card",
                "This is a card",
                "White",
                null,
                null,
                0
        );
        assertNotEquals(card, c1);
    }


    @Test
    void testHashCode() {
        Card c1 = new Card(
                "Some card",
                "This is a card",
                "White",
                null,
                null,
                0
        );

        assertEquals(card.hashCode(), c1.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Card(title=Some card, description=This is a card, " +
                     "backgroundColour=White, tags=null, subTasks=null, id=0)", card.toString());
    }
}
