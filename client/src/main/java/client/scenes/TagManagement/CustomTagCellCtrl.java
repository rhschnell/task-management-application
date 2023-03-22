package client.scenes.TagManagement;


import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
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
    private Circle tagColor;

    private Tag tag;

    @Inject
    public CustomTagCellCtrl(ServerUtils server, TagListCtrl tagListCtrl) {
        this.tagListCtrl = tagListCtrl;
        this.server = server;
    }
    public void setCtrl(TagListCtrl tagListCtrl)
    {
        this.tagListCtrl=tagListCtrl;
    }
    public void setTagObject(Tag tag){
        this.tag = tag;
        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getColor()));
    }
    public void chooseAndClose() {
        tagListCtrl.getCardCtrl().choseTag(tag);
        tagListCtrl.escapeWindow();
    }
}

