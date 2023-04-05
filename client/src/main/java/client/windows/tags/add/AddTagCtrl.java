package client.windows.tags.add;

import client.windows.tags.view.TagOverviewCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddTagCtrl {
    private final AddTagService service;
    private WorkspaceCtrl workspaceCtrl;
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

    /**
     * Constructor for AddTagCtrl
     */
    @Inject
    public AddTagCtrl(AddTagService service, TagOverviewCtrl tagOverviewCtrl) {
        this.service = service;
        this.tagOverviewCtrl = tagOverviewCtrl;
        tagColor = new ColorPicker();
        fontColor = new ColorPicker();
    }

    /**
     * Method to save the newly created tag by the user in the boards tag list
     * This method also refreshes the overview of the tags so that the newly created
     * tag is added immediately
     */
    public void save() {
        ((Stage) addTagButton.getScene().getWindow()).close();
        Tag tag = new Tag(tagTitle.getText(), tagColor.getValue().toString(), fontColor.getValue().toString());
        Board shownBoard = tagOverviewCtrl.getBoard();
        if (shownBoard == null) {
            return;
        }
        shownBoard.addTag(tag);
        service.insertCard(shownBoard.getKey(),tag);
        tagOverviewCtrl.updateDisplayedTags();
    }

    @FXML
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

    /**
     * Setter to set the workSpaceCtrl
     *
     * @param workspaceCtrl The new workspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }
}
