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
                null,
                null
        );
        assertNotNull(card);
    }

    @Test
    void constructorWithID(){
        assertNotNull(card);
    }

    @Test
    void constructorWithPriority(){
        Long priority = 5L;
        card = new Card(
                "My card",
                "Fancy despription",
                null,
                null,
                priority
        );
        assertNotNull(card);
    }

    @Test
    void createCardWithTitle(){
        card = new Card("New card");
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
    void addSubTaskByIndex(){
        Task task = new Task(false, "Task title");
        Task task2 = new Task(true, "Second task");

        card.addSubTask(task);
        card.addSubTask(0, task2);
        assertEquals(List.of(task2, task), card.getSubTasks());
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
    void setBackgroundColour() {
        card.setBackgroundColor("0x000000FF");
        assertEquals("0x000000FF", card.getBackgroundColor());
    }

    @Test
    void getBackgroundColour() {
        assertEquals("0xDEEDE7FF", card.getBackgroundColor());
    }

    @Test
    void hasDescription(){
        assertTrue(card.hasDescription());
    }

    @Test
    void noDescription(){
        card.setDescription(null);
        assertFalse(card.hasDescription());
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
    void getPriority(){
        Long priority = 500L;
        card = new Card(
                "My card",
                "Fancy despription",
                null,
                null,
                priority
        );
        assertEquals(priority, card.getPriority());
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
    void setPriority() {
        card.setPriority(102);
        assertEquals(102, card.getPriority());
    }

    @Test
    void deleteTag(){
        Tag tag = new Tag("New Tag", "0x00000000");
        Tag anotherTag = new Tag("Second Tag", "0x000000FF");
        card.addTag(tag);
        card.addTag(anotherTag);
        card.deleteTag(tag);
        assertEquals(1, card.getTags().size());
    }

    @Test
    void testEquals() {
        Card c1 = new Card(
                "Some card",
                "This is a card",
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
                null,
                null,
                0
        );

        assertEquals(card.hashCode(), c1.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Card(title=Some card, description=This is a card, " +
                "backgroundColor=0xDEEDE7FF, fontColor=0x000000FF, " +
                "preset=CardColorPreset(name=Default, backgroundColor=0xDEEDE7FF, " +
                "fontColor=0x000000FF, isDefault=false, id=0), subTasks=null, " +
                "id=0, priority=0, tags=null)", card.toString());
    }
}
