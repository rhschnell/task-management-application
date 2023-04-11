package client.windows.tags.edit;

import client.serverUtils.BoardUtils;
import client.utils.ErrorDialogEntry;
import client.utils.HelperMethods;
import client.windows.tags.view.CustomEditTagCellCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditTagCtrl {

    private final EditTagService service;

    private CustomEditTagCellCtrl customEditTagCellCtrl;

    private Tag tag;

    @FXML
    private TextField tagTitle;

    @FXML
    private ColorPicker tagColor;
    @FXML
    private ColorPicker fontColor;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;
    private HelperMethods helperMethods;
    private WorkspaceCtrl workspaceCtrl;
    private BoardUtils boardUtils;

    /**
     * Constructor for the EditTagCtrl
     *
     * @param service       Corresponding service
     * @param helperMethods Injected instance of HelperMethods
     * @param boardUtils    to update the Board
     */
    @Inject
    public EditTagCtrl(EditTagService service, HelperMethods helperMethods, BoardUtils boardUtils) {
        this.service = service;
        this.helperMethods = helperMethods;
        this.boardUtils = boardUtils;
    }

    /**
     * Setter for the tag
     *
     * @param tag The new tag
     */
    public void setTag(Tag tag) {
        this.tag = tag;
        setTagTitle(tag.getName());
        setTagColor(Color.web(tag.getTagColor()));
        setFontColor(Color.web(tag.getFontColor()));
    }

    /**
     * Setter for workspaceCtrl
     *
     * @param workspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Setter for the title of the tag
     *
     * @param title The new title of the tag
     */
    public void setTagTitle(String title) {
        tagTitle.setText(title);
    }

    /**
     * Setter for the color of tag
     *
     * @param color The new color of the tag
     */
    public void setTagColor(Color color) {
        tagColor.setId(color.toString());
    }

    /**
     * Setter for the color of font of the tag
     *
     * @param color The new color of the font
     */
    public void setFontColor(Color color) {
        fontColor.setId(color.toString());
    }

    /**
     * Method to set the customTagCellCtrl
     *
     * @param customEditTagCellCtrl The new CustomTagCellCtrl
     */
    public void setCustomTagCellCtrl(CustomEditTagCellCtrl customEditTagCellCtrl) {
        this.customEditTagCellCtrl = customEditTagCellCtrl;
    }

    /**
     * Method to set the color picker in the edit tag popup
     */
    public void setColorPicker() {
        tagColor.setValue(Color.web(tag.getTagColor()));
    }

    /**
     * Sets the Font Color Picker in the edit tag popup
     */
    public void setFontColorPicker() {
        fontColor.setValue(Color.web(tag.getFontColor()));
    }

    /**
     * Method to save the just edited tag
     */
    public void save() {
        String title = helperMethods.getInputValidator().stripWhitespace(tagTitle.getText());

        if (!checkAndHandleInput(title)) {
            tagTitle.requestFocus();
            return;
        }

        Color newTagColor = tagColor.getValue();
        Color newFontColor = fontColor.getValue();
        tagTitle.setText(tag.getName());
        tag.setName(title);
        tag.setTagColor(newTagColor.toString());
        tag.setFontColor(newFontColor.toString());
        service.insertTag(tag);
        workspaceCtrl.updateBoard();
        customEditTagCellCtrl.getTagOverviewCtrl().displayTagList();
        workspaceCtrl.updateBoard();
        boardUtils.insertBoard(boardUtils.getBoard(workspaceCtrl.getBoardKey()));
        ((Stage) saveButton.getScene().getWindow()).close();
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
                    "Your tag name cannot be empty"));
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
     * When pressing enter the edit is saved
     *
     * @param event The key event that needs to be handled
     */
    public void saveOnEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            save();
        }
    }

    /**
     * Method to close the popup window when the cancel button is clicked
     */
    public void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

}
