package client;

import client.scenes.ListManagement.ListCellCtrl;
import commons.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CustomListCell extends AnchorPane {
    private ListCellCtrl controller;

    /**
     * Creates the cell that will be displayed within list.
     *
     * @param card The card associated to this cell
     */

    public CustomListCell(Card card) {
        var loader = new FXMLLoader(getClass().
                getResource("/client/scenes/ListManagement/CustomListCell.fxml"));
        try {
            loader.load();
            controller = loader.getController();
            updateItem(card);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of Cards with a new object.
     *
     * @param item The new item for the cell.
     */

    protected void updateItem(Card item) {
        controller.setCardTitle(item.getTitle());
        controller.setOnButtonClick(event -> {
            System.out.println(item.getTitle());
            // Handle button click
        });
        controller.setDescriptionIconVisible(item.hasDescription());
    }
}