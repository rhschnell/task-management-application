package client.scenes.ListManagement;

import client.scenes.TagManagement.TagListCtrl;
import commons.Card;
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
     * Creates a new instance of ListCellCtrl
     */
    public ListCellCtrl(){}

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

    /**
     * Sets the visibility of the icon that indicates that a card has a description
     * @param visible Boolean indicating the appropriate visibility status of the icon
     */
    public void setDescriptionIconVisible(boolean visible){
        descriptionIcon.setVisible(visible);
    }

    /**
     * Updates the list of Cards with a new object.
     *
     * @param item The new item for the cell.
     */

    protected void updateItem(Card item) {
        setCardTitle(item.getTitle());
        setOnButtonClick(event -> {
            System.out.println(item.getTitle());
            // Handle button click
        });
        setDescriptionIconVisible(item.hasDescription());
    }
}

