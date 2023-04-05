package client.windows.tags.view;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.TagUtils;
import client.utils.HelperMethods;
import client.windows.tags.add.AddTagCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class TagOverviewCtrl {
    private WorkspaceCtrl workspaceCtrl;
    private TagUtils tagUtils;
    private Board board;
    private List<Tag> tagList;

    @FXML
    private VBox displayedTags;

    @FXML
    private Button addTagButton;

    @FXML
    private Button closeButton;


    /**
     * Constructor for the TagOverviewCtrl
     */
    @Inject
    public TagOverviewCtrl(WorkspaceCtrl workspaceCtrl, TagUtils tagUtils) {
        this.workspaceCtrl = workspaceCtrl;
        this.tagUtils = tagUtils;
        this.tagList = new ArrayList<>();
        this.board = workspaceCtrl.getShownBoard();
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
        displayedTags.getChildren().clear();
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
    public void close(){
        ((Stage)closeButton.getScene().getWindow()).close();
        stop();
    }

    /**
     * Setter for the board
     * @param board The new board to be set
     */
    public void setBoard(Board board) {
        this.board = board;

        tagUtils.registerForUpdates(board.getKey(), t -> {
            tagList.add(t);
            displayTagList();
            System.out.println("A new tag was added in pizda matii");
        });
    }

    /**
     * Getter for the board
     * @return The board used
     */
    public Board getBoard(){
        return this.board;
    }

    public void stop() {
        tagUtils.stop();
    }
}
