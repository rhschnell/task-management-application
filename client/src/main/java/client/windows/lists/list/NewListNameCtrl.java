package client.windows.lists.list;

import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewListNameCtrl implements Initializable {

    private WorkspaceCtrl workspaceCtrl;
    @FXML
    private TextField listTitleField;
    private HelperMethods helperMethods;


    /**
     * Creates a new instance of NewListNameCtrl
     * @param helperMethods Instance of HelperMethods that helps display popups in this class
     */
    @Inject
    public NewListNameCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    /**
     * Sets the workspace controller that this links back to
     * MUST be set when this initializes in the workspace controller in order for this to work
     *
     * @param workspaceCtrl The corresponding workspace controller
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Creates a new list with the specified name if that name is not empty by relaying the name
     * to the workspace controller.
     * Called when the user presses enter on the list name text field or the "create" button on
     * the popup window
     */
    @FXML
    private void create() {
        String title = helperMethods.getInputValidator().stripWhitespace(listTitleField.getText());

        if (!helperMethods.validateInputAndShowPopup(title)) return;
        workspaceCtrl.addList(title);
        close();
    }

    /**
     * Makes sure the user can press ENTER when typing in the text field for the list name to
     * create the list and ESCAPE to escape the window
     *
     * @param event The key event that needs to be processed
     */
    @FXML
    private void handleKeyEvents(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            create();
        }
        else if (event.getCode() == KeyCode.ESCAPE){
            cancel();
        }
    }

    /**
     * Makes sure the user can cancel out of creating the new list
     * Called when the user presses ESCAPE or the "cancel" button on the popup window
     */
    @FXML
    public void cancel() {
        close();
    }

    /**
     * Closes this window
     */
    private void close() {
        ((Stage) listTitleField.getScene().getWindow()).close();
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
        Platform.runLater(() -> listTitleField.requestFocus());
    }
}
