package client.windows.lists.list;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.workspace.joinAlerts.EmptyTitleCtrl;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class RenameListCtrl implements Initializable {

    private ListCtrl listCtrl;
    private String previousTitle;

    @FXML
    private TextField newListTitleField;
    private HelperMethods helperMethods;

    /**
     * Creates a new RenameListCtrl
     *
     * @param helperMethods Instance of HelperMethods that helps display popups in this class
     */
    @Inject
    public RenameListCtrl(HelperMethods helperMethods) {
        this.helperMethods = helperMethods;
    }

    /**
     * Sets the list controller that this renaming window is linked to
     * MUST be set when this Rename-window is initialized from within the ListCtrl in order to work
     *
     * @param listCtrl The corresponding list controller
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    /**
     * Sets the previous title for this list to be displayed in the title field
     * MUST be set when this Rename-window is initialized from within the ListCtrl in order to work
     *
     * @param previousTitle The title of the field (before renaming)
     */
    public void setPreviousTitle(String previousTitle) {
        this.previousTitle = previousTitle;
    }

    @FXML
    private void save() {
        String newTitle = newListTitleField.getText();

        if (!helperMethods.validateInputAndShowPopup(newTitle)) return;

        // Apply this new title to the database
        listCtrl.renameList(newTitle);

        // Close the window
        close();
    }

    /**
     * Shows a popup that communicates to the user that the title is not valid
     */
    private void showInvalidTitlePopup() {
        var loader = new MyFXML(createInjector(new MainModules())).load(
                EmptyTitleCtrl.class, "client", "windows", "workspace", "joinAlerts", "EmptyTitle" +
                                                                                      ".fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        helperMethods.popUp(scene, "Invalid title");
    }


    /**
     * Makes sure the user can press ENTER when typing in the text field for the list name to
     * rename the list and ESCAPE to escape the window
     *
     * @param event The key event that needs to be processed
     */
    @FXML
    private void handleKeyEvents(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            save();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            cancel();
        }
    }

    /**
     * Makes sure the user can cancel out of renaming the list
     * Called when the user presses ESCAPE or the "cancel" button on the popup window
     */
    @FXML
    public void cancel() {
        close();
    }

    /**
     * This method is used to close the window.
     */
    private void close() {
        ((Stage) newListTitleField.getScene().getWindow()).close();
    }

    /**
     * Sets the text of the title field
     *
     * @param text The text to set
     */
    public void setTitleText(String text) {
        newListTitleField.setText(text);
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
        Platform.runLater(() -> {
            newListTitleField.setText(previousTitle);
            newListTitleField.selectAll();
            newListTitleField.requestFocus();
        });
    }
}
