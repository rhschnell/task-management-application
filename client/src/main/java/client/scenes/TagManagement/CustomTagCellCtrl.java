package client.scenes.TagManagement;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CustomTagCellCtrl {
    @FXML
    private Label cellTitle;
    @FXML
    private Button deleteButton;

    /**
     * Sets the title of the card shown in the overview of the list
     * @param text
     */
    public void setTagTitle(String text) {
        cellTitle.setText(text);
    }

    /**
     * Sets the event to happen when interacting with the delete button
     * @param handler the event to happen
     */
    public void setOnButtonClick(EventHandler<ActionEvent> handler) {
        deleteButton.setOnAction(handler);
        //here i need the addcard controll i got opened by
    }
}

