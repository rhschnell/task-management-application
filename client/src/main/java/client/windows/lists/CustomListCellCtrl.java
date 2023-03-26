package client.windows.lists;
import commons.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListCellCtrl extends ListCell<Card> {
    private FXMLLoader loader;
    private ListCellCtrl controller;

    /**
     * Creates the cell that will be displayed within list.
     */
    public CustomListCellCtrl() {
        loader = new FXMLLoader(getClass().
                                getResource("/client/windows/lists/ListCell.fxml"));
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
            controller.setCardTitle(item.getTitle());
            controller.setOnButtonClick(event -> {
                System.out.println(item.getTitle());
                // Handle button click
            });
            controller.setDescriptionIconVisible(item.hasDescription());
            setGraphic(loader.getRoot());
        }
    }
}