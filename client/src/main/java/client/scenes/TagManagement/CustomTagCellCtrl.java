package client.scenes.TagManagement;

import client.Main;
import client.MyFXML;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CustomTagCellCtrl {
    @FXML
    private Label cardTitle;
    @FXML
    private Button deleteButton;
    private ServerUtils server;
    private TagListCtrl tagListCtrl;

    private Tag tag;

    @Inject
    public CustomTagCellCtrl(ServerUtils server, TagListCtrl tagListCtrl) {
        //this.server = server;
        this.tagListCtrl = tagListCtrl;

    }

    /**
     * Sets the title of the card shown in the overview of the list
     * @param text
     */
    public void setTagTitle(String text) {
        cardTitle.setText(text);
    }

    public void setCtrl(TagListCtrl tagListCtrl)
    {
        this.tagListCtrl=tagListCtrl;
    }
    public void setTag(Tag tag){this.tag = tag;}
    /**
     * Sets the event to happen when interacting with the delete button
     * @param handler the event to happen
     */
    public void choose() {
       tagListCtrl.getCardCtrl().choseTag(tag);
       System.out.println(tagListCtrl+"TagCell");
        tagListCtrl.cancel();
    }
}

