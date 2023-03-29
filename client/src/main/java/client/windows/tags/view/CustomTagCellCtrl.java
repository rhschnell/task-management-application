package client.windows.tags.view;

import client.windows.cards.add.AddCardCtrl;
import client.serverUtils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CustomTagCellCtrl {
    private TagListCtrl tagListCtrl;
    private AddCardCtrl addCardCtrl;

    @FXML
    private Label tagTitle;

    @FXML
    private ServerUtils server;

    @FXML
    private Button actionButton;

    @FXML
    private Circle tagColor;

    private String type;
    private Tag tag;


    /**
     * Constructor for the CustomTagCellCtrl
     */
    @Inject
    public CustomTagCellCtrl(ServerUtils server, TagListCtrl tagListCtrl,
                             AddCardCtrl addCardCtrl) {
        this.tagListCtrl = tagListCtrl;
        this.addCardCtrl = addCardCtrl;
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
        this.type=type;

        tagTitle.setText(tag.getName());
        tagColor.setFill(Color.web(tag.getColor()));

        if(type.equals("addFromTagList")) {
            actionButton.setText("Add");
            actionButton.getStyleClass().add("blue-button");
        }

        if(type.equals("removeFromTagList") || type.equals("removeFromAddCard")) {
            actionButton.setText("Remove");
            actionButton.getStyleClass().add("red-button");
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

        if(type.equals("removeFromTagList")){
            tagListCtrl.getCardCtrl().removeAppliedTag(tag);
            tagListCtrl.escapeWindow();
        }

        if(type.equals("removeFromAddCard")){
            addCardCtrl.removeAppliedTag(tag);
        }
    }

    /**
     * Setter to set the new tagListCtrl
     * @param tagListCtrl The new tagListCtrl
     */
    public void setTagListCtrl(TagListCtrl tagListCtrl) {
        this.tagListCtrl = tagListCtrl;
    }

    /**
     * Setter to set the new addCardCtrl
     * @param addCardCtrl The new addCardCtrl
     */
    public void setAddCardCtrl(AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
    }
}

