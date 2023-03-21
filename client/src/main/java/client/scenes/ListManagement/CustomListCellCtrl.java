package client.scenes.ListManagement;

import client.scenes.TagManagement.TagListCtrl;
import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CustomListCellCtrl {
    @FXML
    private Label cardTitle;
    @FXML
    private Button deleteButton;

    private TagListCtrl tagListCtrl;

    /**
     * Sets the title of the card shown in the overview of the list
     * @param text
     */
    public void setCardTitle(String text) {
        cardTitle.setText(text);
    }

    /**
     * Sets the event to happen when interacting with the delete button
     * @param handler the event to happen
     */
    public void setOnButtonClick(EventHandler<ActionEvent> handler) {
        deleteButton.setOnAction(handler);
    }
}

