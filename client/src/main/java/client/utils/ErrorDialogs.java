package client.utils;

public class ErrorDialogs {

    public final static ErrorDialogEntry INVALID_LENGTH =
            new ErrorDialogEntry(
                    "Error!",
                    "Your input exceeds the maximum length of " + HelperMethods.maxInputLength);

    public final static ErrorDialogEntry INVALID_EMPTY =
            new ErrorDialogEntry(
                    "Error!",
                    "Your input cannot be empty");

    public final static ErrorDialogEntry INVALID_START_WHITESPACE =
            new ErrorDialogEntry("Error!",
                    "Your input must not start with a whitespace character");
}
