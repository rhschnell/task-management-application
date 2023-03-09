package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    public Tag generateTag(){
        return new Tag("Frontend", "Blue");
    }
    @Test
    void getName() {
        assertEquals("Frontend", generateTag().getName());
    }

    @Test
    void setName() {
        Tag tag = generateTag();
        tag.setName("Backend");
        assertEquals("Backend", tag.getName());
    }

    @Test
    void getColor() {
        assertEquals("Blue", generateTag().getColor());
    }

    @Test
    void setColor() {
        Tag tag = generateTag();
        tag.setColor("Orange");
        assertEquals("Orange", tag.getColor());
    }

    @Test
    void testEqualsSame() {
        Tag tag1 = generateTag();
        Tag tag2 = generateTag();
        assertEquals(tag1, tag2);
    }

    @Test
    void testEqualsSameObject(){
        Tag tag = generateTag();
        assertEquals(tag, tag);
    }


    @Test
    void testEqualsDifferentName(){
        Tag tag1 = generateTag();
        Tag tag2 = generateTag();
        tag2.setName("Another name");
        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEqualsDifferentColor(){
        Tag tag1 = generateTag();
        Tag tag2 = generateTag();
        tag2.setColor("Another color");
        assertNotEquals(tag1, tag2);
    }

    @Test
    void testEqualsNull(){
        Tag tag = generateTag();
        assertNotEquals(tag, null);
    }


    @Test
    void testHashCode() {
        Tag tag1 = generateTag();
        Tag tag2 = generateTag();
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testToString(){
        Tag tag = generateTag();
        assertEquals("Tag{name='Frontend', color='Blue'}", tag.toString());
    }
}