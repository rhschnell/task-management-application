package client.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDialogEntryTest {

    private ErrorDialogEntry errorDialogEntry;
    @BeforeEach
    void setUp() {
        this.errorDialogEntry = new ErrorDialogEntry("Title", "Error message");
    }

    @Test
    void getPopupTitle() {
        assertEquals("Title", errorDialogEntry.getPopupTitle());
    }

    @Test
    void getMessage() {
        assertEquals("Error message", errorDialogEntry.getMessage());
    }

    @Test
    void setPopupTitle() {
        errorDialogEntry.setPopupTitle("New title");
        assertEquals("New title", errorDialogEntry.getPopupTitle());
    }

    @Test
    void setMessage() {
        errorDialogEntry.setMessage("New message");
        assertEquals("New message", errorDialogEntry.getMessage());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(errorDialogEntry, errorDialogEntry);
    }

    @Test
    void testEqualsSameValues(){
        assertEquals(errorDialogEntry, new ErrorDialogEntry("Title", "Error message"));
    }

    @Test
    void testEqualsNull(){
        assertNotEquals(errorDialogEntry, null);
    }

    @Test
    void testEqualsDifferentValues(){
        assertNotEquals(errorDialogEntry, new ErrorDialogEntry("Different title", "Different " +
                                                                                  "message"));
    }

    @Test
    void testHashCode() {
        // Create new object same
        ErrorDialogEntry sameAsDefault = new ErrorDialogEntry("Title", "Error message");
        assertEquals(errorDialogEntry.hashCode(), sameAsDefault.hashCode());
    }
}
