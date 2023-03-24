package client.scenes.TagManagement;


import client.scenes.CardWindows.AddCardCtrl;
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
    private AddCardCtrl addCardCtrl;

    @FXML
    private Button actionButton;
    @FXML
    private Circle tagColor;

    private String type;

    private Tag tag;


    @Inject
    public CustomTagCellCtrl(ServerUtils server, TagListCtrl tagListCtrl, AddCardCtrl addCardCtrl) {
        this.tagListCtrl = tagListCtrl;
        this.addCardCtrl=addCardCtrl;
        this.server = server;
    }

    /**
     * The tag Type is Set so that when the FXML is created we know which button to display,
     * moreover,the tag is set so that when we send the add/remove information we know which
     * tag was pressed.
     * @param tag the has that needs to be later added/deleted
     * @param type the type of the TagCell FXML we need to display
     */
    public void setTagObject(Tag tag, String type){
        this.tag = tag;
        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getColor()));
        this.type=type;
        if(type.equals("addFromTagList"))
        {
            actionButton.setText("Add");actionButton.getStyleClass().add("blue-button");
        }
        if(type.equals("removeFromTagList") || type.equals("removeFromAddCard"))
        {
            actionButton.setText("Remove");actionButton.getStyleClass().add("red-button");
        }
    }

    /**
     * The action of Removing/Adding is removed by sending
     * the information to the controller it came and
     * the pop-up window is closed
     */
    public void chooseAndClose() {
        if(type.equals("addFromTagList")) {
            tagListCtrl.getCardCtrl().applyTag(tag);
            tagListCtrl.escapeWindow();
        }
        if(type.equals("removeFromTagList") )
        {
            tagListCtrl.getCardCtrl().removeAppliedTag(tag);
            tagListCtrl.escapeWindow();
        }
        if(type.equals("removeFromAddCard"))
        {
            addCardCtrl.removeAppliedTag(tag);
        }
    }

    public void setCtrl(TagListCtrl tagListCtrl) {
        this.tagListCtrl = tagListCtrl;
    }
    public void setCtrl2(AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
    }
}

