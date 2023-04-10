package client.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    private InputValidator inputValidator;

    @BeforeEach
    void setUp(){
        this.inputValidator = new InputValidator();
    }

    @Test
    void stripWhitespace(){
        assertEquals("without any leading or trailing whitespace?",
                inputValidator.stripWhitespace("   without any leading or trailing whitespace?  "));

    }

    @Test
    void isValidInputNonStartingWhitespaceFail() {
        assertFalse(inputValidator.isValidInputNonStartingWhitespace(" starts with whitespace"));
    }

    @Test
    void isValidInputNonStartingWhitespaceSuccess() {
        assertTrue(inputValidator.isValidInputNonStartingWhitespace("Does not start with " +
                                                                    "whitespace"));
    }

    @Test
    void isValidInputNonEmptyFail() {
        assertFalse(inputValidator.isValidInputNonEmpty(""));
    }

    @Test
    void isValidInputNonEmptySuccess() {
        assertTrue(inputValidator.isValidInputNonEmpty("Not empty"));
    }

    @Test
    void isValidInputLengthFail() {
        inputValidator.setMaxInputLength(2);
        assertFalse(inputValidator.isValidInputLength("123"));
    }

    @Test
    void isValidInputLengthSuccessEdgeCase() {
        inputValidator.setMaxInputLength(2);
        assertTrue(inputValidator.isValidInputLength("12"));
    }

    @Test
    void isValidInputLengthSuccess() {
        inputValidator.setMaxInputLength(2);
        assertTrue(inputValidator.isValidInputLength("1"));
    }
}