package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDialogsTest {

    @Test
    public void testInvalidLengthEntry() {
        ErrorDialogEntry entry = ErrorDialogs.INVALID_LENGTH;
        assertEquals("Error!", entry.getPopupTitle());
        assertEquals("Your input exceeds the maximum length of " + HelperMethods.getMaxInputLength(), entry.getMessage());
    }

    @Test
    public void testInvalidEmptyEntry() {
        ErrorDialogEntry entry = ErrorDialogs.INVALID_EMPTY;
        assertEquals("Error!", entry.getPopupTitle());
        assertEquals("Your input cannot be empty", entry.getMessage());
    }

    @Test
    public void testInvalidStartWhitespaceEntry() {
        ErrorDialogEntry entry = ErrorDialogs.INVALID_START_WHITESPACE;
        assertEquals("Error!", entry.getPopupTitle());
        assertEquals("Your input must not start with a whitespace character", entry.getMessage());
    }
}
