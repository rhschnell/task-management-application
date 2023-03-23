package client.scenes.TagManagement;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class CustomTagCellCtrl {
    @FXML
    private Label tagTitle;
    @FXML
    private ServerUtils server;
    private TagListCtrl tagListCtrl;

    @FXML
    private Button actionButton;
    @FXML
    private Circle tagColor;

    private boolean type;

    private Tag tag;


    @Inject
    public CustomTagCellCtrl(ServerUtils server, TagListCtrl tagListCtrl) {
        this.tagListCtrl = tagListCtrl;
        this.server = server;
    }
    public void setTagObject(Tag tag, Boolean type){
        this.tag = tag;
        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getColor()));
        this.type=type;
        if(type)
        {actionButton.setText("Add");actionButton.getStyleClass().add("blue-button");}
        else
        {actionButton.setText("Remove");actionButton.getStyleClass().add("red-button");}
    }
    public void chooseAndClose() {
        if(type) {
            tagListCtrl.getCardCtrl().applyTag(tag);
            tagListCtrl.escapeWindow();
        }
        else
        {
            tagListCtrl.getCardCtrl().removeAppliedTag(tag);
            tagListCtrl.escapeWindow();
        }
    }
}

