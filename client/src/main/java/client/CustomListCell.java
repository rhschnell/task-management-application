package client;
import client.scenes.ListManagement.CustomListCellCtrl;
import commons.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomListCell extends ListCell<Card> {
    private FXMLLoader loader;
    private CustomListCellCtrl controller;

    public CustomListCell() {
        loader = new FXMLLoader(getClass().getResource("/client/scenes/ListManagement/CustomListCell.fxml"));
        try {
            loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            setGraphic(loader.getRoot());
        }
    }
}