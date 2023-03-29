package client.windows.tags.view;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.tags.add.AddTagCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class TagOverviewCtrl {
    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private VBox displayedTags;

    @FXML
    private Button addTagButton;

    @FXML
    private Button cancelButton;


    /**
     * Constructor for the TagOverviewCtrl
     */
    @Inject
    public TagOverviewCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Method to open a new popup window where the user can add a tag
     */
    public void addTag() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AddTagCtrl.class, "client", "windows", "tags", "AddTag.fxml");
        loader.getKey().set(this);
        loader.getKey().setWorkspaceCtrl(workspaceCtrl);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Add Tag";
        HelperMethods.popUp(scene, title);
    }

    /**
     * Method to display the tags that are currently added to the board by the user
     */
    public void displayTagList() {
        workspaceCtrl.refreshWorkspace();
        Board shownBoard = workspaceCtrl.getShownBoard();

        List<Tag> tagList = shownBoard.getTagList();

        for (Tag tag : tagList) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomEditTagCellCtrl.class, "client", "windows", "tags", "CustomEditTagCell.fxml");
            CustomEditTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(tag);
            ctrl.setTagOverviewCtrl(this);

            displayedTags.getChildren().add(loader.getValue());
        }
    }

    /**
     * Method to update the displayed tags from the board in the VBox
     */
    public void updateDisplayedTags(){
        displayedTags.getChildren().clear();
        displayTagList();
    }

    /**
     * Method to close the popup window when the cancel button is pressed
     */
    public void cancel(){
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }
}
