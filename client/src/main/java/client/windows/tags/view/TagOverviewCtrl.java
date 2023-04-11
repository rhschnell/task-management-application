package client.windows.tags.view;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.BoardUtils;
import client.serverUtils.TagUtils;
import client.utils.HelperMethods;
import client.windows.tags.add.AddTagCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class TagOverviewCtrl {
    private final HelperMethods helperMethods;
    private final TagUtils tagUtils;
    private String boardKey;
    private List<Tag> tagList;

    @FXML
    private VBox displayedTags;

    @FXML
    private Button addTagButton;
    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private Button closeButton;
    private BoardUtils boardUtils;

    /**
     * Constructor for the TagOverviewCtrl
     * @param helperMethods hm
     * @param tagUtils Instance of the utility class for tags
     * @param boardUtils Intance of the boardUtils to also update the board when a tag update is received
     */
    @Inject
    public TagOverviewCtrl(HelperMethods helperMethods, TagUtils tagUtils, BoardUtils boardUtils) {
        this.helperMethods = helperMethods;
        this.tagUtils = tagUtils;
        this.tagList = new ArrayList<>();
        this.boardUtils = boardUtils;
    }

    /**
     * Method to open a new popup window where the user can add a tag
     */
    public void addTag() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AddTagCtrl.class, "client", "windows", "tags", "AddTag.fxml");
        loader.getKey().set(this);
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add Tag";
        helperMethods.popUp(scene, title);
    }

    /**
     * Method to display the tags that are currently added to the board by the user
     */
    public void displayTagList() {
        //tagList = tagUtils.getBoardTags(boardKey);
        displayedTags.getChildren().clear();
        for (int i = 0; i < tagList.size(); i++) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomEditTagCellCtrl.class, "client", "windows", "tags", "CustomEditTagCell.fxml");
            CustomEditTagCellCtrl ctrl = loader.getKey();
            ctrl.setWorkspaceCtrl(workspaceCtrl);
            ctrl.setTagObject(tagList.get(i));
            ctrl.setTagOverviewCtrl(this);
            displayedTags.getChildren().add(loader.getValue());
        }
    }

    /**
     * Method to close the popup window when the cancel button is pressed
     */
    public void close() {
        stop();
        ((Stage)closeButton.getScene().getWindow()).close();
    }

    /**
     * Setter for the board
     * @param boardKey The new board to be set
     */
    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Calls the stop method from tagUtils, closing the execution of the
     * thread
     */
    public void stop() {
        tagUtils.stop();
    }

    /**
     * Calls the poll method that subscribes to the long polling so that the client
     * can receive updates when another tag is added on the board
     */
    public synchronized void poll() {
        tagList = tagUtils.getBoardTags(boardKey);
        displayTagList();
        tagUtils.registerForUpdates(boardKey, tagList, t -> {
            Platform.runLater(this::displayTagList);
            //workspaceCtrl.refreshWorkspace(false);
            boardUtils.insertBoard(boardUtils.getBoard(boardKey));

        });
    }

    /**
     * Setter for the workspace in order to send a message the board has updates
     * @param workspaceCtrl the workspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Gets the boardKey
     * @return the boardKey
     */
    public String getBoardKey() {
        return boardKey;
    }
}
