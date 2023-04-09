package client.utils;

import client.MyFXML;
import client.modules.MainModules;
import client.windows.dialogs.DialogPopupCtrl;
import client.windows.workspace.helpWindow.HelpWindowCtrl;
import com.google.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Set;

import static client.utils.ErrorDialogs.*;
import static com.google.inject.Guice.createInjector;

public class HelperMethods {
    private Stage primaryStage;
    private Scene[] scenes;
    private DataFormat cardFormat;
    private Map<String, Set<String>> memMap;
    private String serverIP;
    private InputValidator inputValidator;
    private static final int MAX_INPUT_LENGTH = 255;
    /**
     * Creates a new HelperMethods instance
     * @param inputValidator Validator for user input
     */
    @Inject
    public HelperMethods(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
        inputValidator.setMaxInputLength(MAX_INPUT_LENGTH);
    }

    /**
     * Returns the (static) max input length that is allowed for user input
     * @return The maximum allowed input length
     */
    public static int getMaxInputLength() {
        return MAX_INPUT_LENGTH;
    }

    /**
     * Sets the data format of the card, used in dragging and dropping cards
     *
     * @param cardFormat The new card format
     */
    public void setCardFormat(DataFormat cardFormat) {
        this.cardFormat = cardFormat;
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     *
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     */
    public void popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        if (!title.equals("Help Window")) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SLASH && event.isShiftDown()) {
                    var loader = new MyFXML(createInjector(new client.modules.MainModules()))
                            .load(HelpWindowCtrl.class, "client", "windows",
                                    "workspace", "helpWindow", "helpWindow.fxml");
                    Parent root = loader.getValue();
                    Scene helpScene = new Scene(root);
                    String helpTitle = "Help Window";
                    this.popUp(helpScene, helpTitle);
                }

            });
        }
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.showAndWait();
    }

    /**
     * Gets the card format
     *
     * @return the card format
     */
    public DataFormat getCardFormat() {
        return cardFormat;
    }

    /**
     * Sets the scene
     *
     * @param s the scene to set
     */
    public void setScene(Scenes s) {
        int scene;
        switch (s) {
            case STARTUP:
                scene = 0;
                break;
            case USER:
                scene = 3;
                break;
            case ADMIN:
                scene = 1;
                break;
            case WORKSPACE:
                scene = 2;
                break;
            case ADMINVIEW:
                scene = 4;
                break;
            default:
                throw new IllegalArgumentException();
        }
        primaryStage.setScene(scenes[scene]);
    }

    /**
     * Sets the primary stage
     *
     * @param scene the scene to set as a primary stage
     */
    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * Sets the scenes
     *
     * @param scenes the scenes to set
     */
    public void setScenes(Scene... scenes) {
        this.scenes = scenes;
    }

    /**
     * Sets the primary stage
     *
     * @param primaryStage the primary stage to set
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SLASH && event.isShiftDown()) {
                var loader = new MyFXML(createInjector(new client.modules.MainModules()))
                        .load(HelpWindowCtrl.class, "client", "windows",
                                "workspace", "helpWindow", "helpWindow.fxml");
                Parent root = loader.getValue();
                Scene helpScene = new Scene(root);
                String helpTitle = "Help Window";
                this.popUp(helpScene, helpTitle);
            }

        });
    }

    /**
     * Sets the memory map
     *
     * @param serverToKeyListMap New map containing servers as keys and lists or board-keys as
     *                           values
     */
    public void setMemMap(Map<String, Set<String>> serverToKeyListMap) {
        this.memMap = serverToKeyListMap;
    }

    /**
     * Returns the map containing combinations of server IP addresses and corresponding boards
     * hosted on that server that the client has connected with.
     *
     * @return The memory map
     */
    public Map<String, Set<String>> getMemMap() {
        return memMap;
    }

    /**
     * Gets the IP address of the server
     *
     * @return The server's IP address
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Sets the IP address of the server
     *
     * @param serverIP The new server IP address
     */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }


    /**
     * This method validates the given text and displays a popup to communicate invalid input
     * back to the user.
     *
     * The methods to validate the text are used in order of importance.
     * Example: if the user gives an empty input, then the input is both starting with whitespace
     * and empty. Since input not allowed to be empty one is a more general rule than it not
     * being allowed to start with whitespaces, this is also the message displayed to the user.
     *
     * @param textToValidate The text to validate
     * @return Boolean indicating the correctness of this text
     *
     * @see #isValidInputNonEmpty(String)
     * @see #isValidInputNonStartingWhitespace(String)
     * @see #isValidInputLength(String)
     * @see ErrorDialogs
     */
    public boolean validateInputAndShowPopup(String textToValidate){
        if (textToValidate == null) return false;
        if (!inputValidator.isValidInputLength(textToValidate)) {
            showErrorDialog(INVALID_LENGTH);
            return false;
        }

        if (!inputValidator.isValidInputNonEmpty(textToValidate)){
            showErrorDialog(INVALID_EMPTY);
            return false;
        }

        if (!inputValidator.isValidInputNonStartingWhitespace(textToValidate)) {
            showErrorDialog(INVALID_START_WHITESPACE);
            return false;
        }

        return true;
    }

    /**
     * Shows a simple dialog popup with a title, message and close button. The title and message
     * are specified in the entry
     * @param errorDialogEntry ErrorDialogEntry containing the title and message of this error dialog
     */
    public void showErrorDialog(ErrorDialogEntry errorDialogEntry){
        var loader = new MyFXML(createInjector(new MainModules())).load(
                DialogPopupCtrl.class, "client", "windows", "dialogs", "DialogPopup.fxml");

        DialogPopupCtrl ctrl = loader.getKey();
        ctrl.setMessage(errorDialogEntry.getMessage());

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        popUp(scene, errorDialogEntry.getPopupTitle());
    }

    /**
     * Returns the input validator
     * @return The input validator object
     */
    public InputValidator getInputValidator() {
        return inputValidator;
    }
}

