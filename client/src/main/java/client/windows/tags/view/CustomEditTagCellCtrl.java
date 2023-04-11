package client.windows.tags.view;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.BoardUtils;
import client.serverUtils.TagUtils;
import client.utils.HelperMethods;
import client.windows.tags.edit.EditTagCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import static com.google.inject.Guice.createInjector;

public class CustomEditTagCellCtrl {
    private TagOverviewCtrl tagOverviewCtrl;
    private final HelperMethods helperMethods;

    @FXML
    private TagUtils server;

    @FXML
    private Label tagTitle;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;
    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private Circle tagColor;

    private Tag tag;
    private BoardUtils boardUtils;

    /**
     * Constructor for the CustomEditTagCellCtrl
     * @param server The server that handles tags
     * @param tagOverviewCtrl The controller that this cell links back to
     * @param helperMethods hm
     * @param boardUtils The instance of the utility class that handles boards
     */
    @Inject
    public CustomEditTagCellCtrl(TagUtils server, TagOverviewCtrl tagOverviewCtrl,
                                 HelperMethods helperMethods, BoardUtils boardUtils){
        this.server = server;
        this.tagOverviewCtrl = tagOverviewCtrl;
        this.helperMethods = helperMethods;
        this.boardUtils=boardUtils;
    }

    /**
     * Displays the tag
     * @param tag The tag to be displayed
     */
    public void setTagObject(Tag tag){
        this.tag = tag;
        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getTagColor()));
        tagTitle.setTextFill(Paint.valueOf(tag.getFontColor()));
        editButton.getStyleClass().add("blue-button");
        deleteButton.getStyleClass().add("red-button");

    }

    /**
     * Setter for workspaceCtrl
     * @param workspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
    }

    /**
     * Method to delete a tag from the board
     */
    public void deleteTag(){
        server.deleteTag(tag.getId());
        boardUtils.removeBoardTag(tagOverviewCtrl.getBoardKey(),tag);

    }

    /**
     * Method to create a new popup window where the user can edit a tag
     */
    public void editTag(){
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(EditTagCtrl.class, "client", "windows", "tags", "EditTag.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        EditTagCtrl controller = loader.getKey();
        controller.setWorkspaceCtrl(workspaceCtrl);
        controller.setTag(tag);
        controller.setColorPicker();
        controller.setFontColorPicker();
        controller.setCustomTagCellCtrl(this);

        String title = "Edit Tag";
        helperMethods.popUp(scene, title);
    }

    /**
     * Getter to get the tagOverviewCtrl
     * @return the tagOverviewCtrl
     */
    public TagOverviewCtrl getTagOverviewCtrl() {
        return this.tagOverviewCtrl;
    }

    /**
     * Setter to set the tagOverviewCtrl
     * @param tagOverviewCtrl The new tagOverviewCtrl
     */
    public void setTagOverviewCtrl(TagOverviewCtrl tagOverviewCtrl) {
        this.tagOverviewCtrl = tagOverviewCtrl;
    }
}
