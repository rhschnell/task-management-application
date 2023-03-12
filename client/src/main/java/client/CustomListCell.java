package client;
import client.scenes.ListManagement.CustomListCellCtrl;
import commons.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListCell extends ListCell<Card> {
    private FXMLLoader loader;
    private CustomListCellCtrl controller;

    /**
     * Creates the cell that will be displayed within list.
     */
    public CustomListCell() {
        loader = new FXMLLoader(getClass().getResource("/client/scenes/ListManagement/CustomListCell.fxml"));
        try {
            loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of Cards with a new object.
     * @param item The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Card item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            controller.setText(item.getTitle());
            controller.setOnButtonClick(event -> {
                System.out.println(item.getTitle());
                // Handle button click
            });
            setGraphic(loader.getRoot());
        }
    }
}