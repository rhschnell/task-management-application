package client.windows.tags.add;

import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.tags.view.TagOverviewCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddTagCtrl {
    private final AddTagService service;
    private TagOverviewCtrl tagOverviewCtrl;

    @FXML
    private TextField tagTitle;

    @FXML
    private ColorPicker tagColor;
    @FXML
    private ColorPicker fontColor;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addTagButton;
    private HelperMethods helperMethods;

    /**
     * Constructor for AddTagCtrl
     *
     * @param service         The service for handling adding tags
     * @param tagOverviewCtrl The controller of the corresponding TagOverview view
     * @param helperMethods   The injected instance of HelperMethods
     */
    @Inject
    public AddTagCtrl(AddTagService service, TagOverviewCtrl tagOverviewCtrl, HelperMethods helperMethods) {
        this.service = service;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.helperMethods = helperMethods;
        tagColor = new ColorPicker();
        fontColor = new ColorPicker();
    }

    /**
     * Method to save the newly created tag by the user in the boards tag list
     * This method also refreshes the overview of the tags so that the newly created
     * tag is added immediately
     */
    public void save() {

        String title = helperMethods.getInputValidator().stripWhitespace(tagTitle.getText());

        if (!checkAndHandleInput(title)) {
            tagTitle.requestFocus();
            return;
        }

        ((Stage) addTagButton.getScene().getWindow()).close();


        Tag tag = new Tag(title, tagColor.getValue().toString(), fontColor.getValue().toString());
        service.insertTag(tagOverviewCtrl.getBoardKey(), tag);
        tagOverviewCtrl.displayTagList();
    }

    /**
     * Checks the user input and shows error messages accordingly
     * @param title The title to check
     */
    private boolean checkAndHandleInput(String title) {
        if (!helperMethods.getInputValidator().isValidInputNonEmpty(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry("Error!", "Your tag name cannot " +
                                                                         "be empty"));
            return false;
        }
        if (!helperMethods.getInputValidator().isValidInputLength(title)) {
            helperMethods.showErrorDialog(new ErrorDialogEntry(
                    "Error!",
                    "Your tag name cannot be longer than " + HelperMethods.getMaxInputLength()));
            return false;
        }
        return true;
    }


    /**
     * Methods that handles a keyEvent to include saving on enter
     *
     * @param event The event that needs to be handled
     */
    public void saveOnEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            save();
        }
    }

    /**
     * Method to cancel the popup window when the cancel button is pressed
     */
    public void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    /**
     * Setter to set the tagOverviewCtrl
     *
     * @param tagOverviewCtrl The new tagOverviewCtrl
     */
    public void set(TagOverviewCtrl tagOverviewCtrl) {
        this.tagOverviewCtrl = tagOverviewCtrl;
    }
}
