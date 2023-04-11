package client.windows.workspace.rename;

import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RenameCtrl implements Initializable {
    private final RenameService service;

    private WorkspaceCtrl workspaceCtrl;
    private HelperMethods helperMethods;

    @FXML
    private TextField inputField;

    /**
     * Injectable constructor for the RenameCtrl
     * After using this constructor setWorkspaceCtrl MUST be called.
     *
     * @param service       Injected parameter of corresponding service
     * @param helperMethods Injected instance of HelperMethods
     */
    @Inject
    public RenameCtrl(RenameService service, HelperMethods helperMethods) {

        this.service = service;
        this.helperMethods = helperMethods;

    }

    /**
     * Setter for workspaceCtrl, MUST be called after constructor.
     *
     * @param workspaceCtrl the instance to be injected
     */
    public void setRemoteCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
        this.inputField.setText(workspaceCtrl.getShownBoard().getTitle());
    }

    /**
     * This method is executed upon clicking the save button in the popup.
     * It sets the title of the board to the current text of the
     * inputField and then inserts this into the database through
     * the service.
     * Then it closes the window.
     */
    public void save() {
        Board board = workspaceCtrl.getShownBoard();

        String newTitle = helperMethods.getInputValidator().stripWhitespace(inputField.getText());
        if (!checkAndHandleInput(newTitle)) return;

        board.setTitle(newTitle);
        service.insertBoard(board);
        close();
    }

    /**
     * Checks the user input and shows error messages accordingly
     *
     * @param title The title to check
     */
    private boolean checkAndHandleInput(String title) {
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your board title cannot be empty"));
            return false;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your board name cannot be longer than " + HelperMethods.getMaxInputLength()));
            return false;
        }
        return true;
    }

    /**
     * Listener for the ENTER KeyEvent so when pressed the change of name can be
     * saved
     *
     * @param event The event to handle
     */
    public void saveOnEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            save();
        }
    }

    /**
     * This method is executed upon clicking the cancel button in the popup.
     * It clears the inputField and closes the window.
     */
    public void cancel() {
        inputField.clear();
        close();
    }

    /**
     * This method is used to close the window.
     */
    private void close() {
        ((Stage) inputField.getScene().getWindow()).close();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> inputField.requestFocus());
    }
}

