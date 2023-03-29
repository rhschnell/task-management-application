package client.windows.tags.view;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.TagUtils;
import client.utils.HelperMethods;
import client.windows.tags.edit.EditTagCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.google.inject.Guice.createInjector;

public class CustomEditTagCellCtrl {
    private TagOverviewCtrl tagOverviewCtrl;

    @FXML
    private TagUtils server;

    @FXML
    private Label tagTitle;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Circle tagColor;

    private Tag tag;

    /**
     * Constructor for the CustomEditTagCellCtrl
     */
    @Inject
    public CustomEditTagCellCtrl(TagUtils server, TagOverviewCtrl tagOverviewCtrl){
        this.server = server;
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    /**
     * Displays the tag
     * @param tag The tag to be displayed
     */
    public void setTagObject(Tag tag){
        this.tag = tag;
        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getColor()));

        editButton.getStyleClass().add("blue-button");
        deleteButton.getStyleClass().add("red-button");

    }

    /**
     * Method to delete a tag from the board
     */
    public void deleteTag(){
        server.deleteTag(tag.getId());

        Board shownBoard = tagOverviewCtrl.getBoard();
        shownBoard.removeTag(tag);

        tagOverviewCtrl.updateDisplayedTags();
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
        controller.setTag(tag);
        controller.setCustomTagCellCtrl(this);

        String title = "Edit Tag";
        HelperMethods.popUp(scene, title);
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
