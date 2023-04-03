package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Tag tag;

    @BeforeEach
    public void generateTag() {
        this.tag = new Tag("Frontend", "Blue");
    }

    @Test
    void testNoArgsConstructor() {
        // Just testing object creation
        new Tag();
    }

    @Test
    void testAllArgsConstructor() {
        // Just testing object creation
        new Tag("Name", "tagColor", "fontColor", 1, new ArrayList<>());
    }

    @Test
    void testGetName() {
        assertEquals("Frontend", tag.getName());
    }

    @Test
    void testSetName() {
        tag.setName("Backend");
        assertEquals("Backend", tag.getName());
    }

    @Test
    void testGetTagColor() {
        assertEquals("Blue", tag.getTagColor());
    }

    @Test
    void testSetTagColor() {
        tag.setTagColor("Orange");
        assertEquals("Orange", tag.getTagColor());
    }

    @Test
    void testGetFontColor() {
        assertEquals("Blue", tag.getTagColor());
    }

    @Test
    void testSetFontColor() {
        tag.setFontColor("Orange");
        assertEquals("Orange", tag.getFontColor());
    }

    @Test
    void testGetId() {
        assertEquals(0, tag.getId());
    }

    @Test
    void testSetId() {
        tag.setId(2);
        assertEquals(2, tag.getId());
    }

    @Test
    void testGetCardList() {
        assertEquals(new ArrayList<Card>(), tag.getCards());
    }

    @Test
    void testSetCardList() {
        Card card1 = new Card();
        Card card2 = new Card();

        tag.setCards(List.of(
                card1,
                card2
        ));

        assertEquals(List.of(card1, card2), tag.getCards());
    }

    @Test
    void testEqualsSame() {
        Tag tag1 = new Tag("Frontend", "Blue");
        assertEquals(tag, tag1);
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(tag, tag);
    }


    @Test
    void testEqualsDifferentName() {
        Tag tag1 = new Tag("Another name", "Blue");
        assertNotEquals(tag, tag1);
    }

    @Test
    void testEqualsDifferentColor() {
        Tag tag1 = new Tag("Frontend", "Another color");
        assertNotEquals(tag, tag1);
    }

    @Test
    void testEqualsNull() {
        assertNotNull(tag);
    }


    @Test
    void testHashCode() {
        Tag tag1 = new Tag("Frontend", "Blue");
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Tag{name='Frontend', tagColor='Blue', fontColor='Black', id=0, cards=[]}", tag.toString());
    }
}