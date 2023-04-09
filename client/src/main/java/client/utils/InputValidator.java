package client.utils;

import java.util.regex.Pattern;

public class InputValidator {

    private int maxInputLength;
    private final Pattern whitespacePattern;

    /**
     * Creates a new InputValidator
     */
    public InputValidator(){
        maxInputLength = 255;
        // Has to start with a non-whitespace character
        whitespacePattern = Pattern.compile("^\\S.*");
    }

    /**
     * Method to strip text from leading and trailing whitespaces
     * @param input The text to strip
     * @return The stripped input
     */
    public String stripWhitespace(String input){
        return input.strip();
    }

    /**
     * Sets the maximum input length that is considered valid
     * @param maxInputLength The maximum input length
     */
    public void setMaxInputLength(int maxInputLength) {
        this.maxInputLength = maxInputLength;
    }

    /**
     * Method to validate the input of anything to be starting with whitespace.
     *
     * @param text The text to validate
     * @return Boolean indicating the validness of the given text according to the conditions
     * mentioned above
     */
    public boolean isValidInputNonStartingWhitespace(String text) {
        return (whitespacePattern.matcher(text).find());
    }

    /**
     * Method to validate the text not being empty
     * @param text The text to validate
     * @return Boolean indicating the validness of the given text according to the conditions
     * mentioned above
     */
    public boolean isValidInputNonEmpty(String text){
        return !text.equals("");
    }

    /**
     * Method to validate the length of any input to be within the set bounds
     * @param text The text to validate
     * @return Boolean indicating the validness of the given text according to the conditions
     * mentioned above
     */
    public boolean isValidInputLength(String text){
        return text.length() <= maxInputLength;
    }
}
