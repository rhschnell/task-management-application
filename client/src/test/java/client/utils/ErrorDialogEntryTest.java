package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDialogEntryTest {

    @Test
    void testConstructorAndGetters() {
        String title = "Title";
        String message = "Message";
        ErrorDialogEntry entry = new ErrorDialogEntry(title, message);
        assertEquals(title, entry.getPopupTitle());
        assertEquals(message, entry.getMessage());
    }
}
