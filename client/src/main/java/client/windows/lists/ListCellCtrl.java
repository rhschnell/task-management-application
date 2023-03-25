package client.windows.lists;

import client.windows.tags.TagListCtrl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ListCellCtrl {
    @FXML
    private Label cardTitle;
    @FXML
    private Button deleteButton;

    private TagListCtrl tagListCtrl;

    @FXML
    private ImageView descriptionIcon;
    /**
     * Sets the title of the card shown in the overview of the list
     * @param text text to set title to
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

    /**
     * Sets the visibility of the icon that indicates that a card has a description
     * @param visible Boolean indicating the appropriate visibility status of the icon
     */
    public void setDescriptionIconVisible(boolean visible){
        descriptionIcon.setVisible(visible);
    }
}

