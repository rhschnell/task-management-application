package client;
import client.scenes.CardCellController;
import commons.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CardCell extends ListCell<Card> {
    private FXMLLoader loader;
    private CardCellController controller;

    public CardCell() {
        loader = new FXMLLoader(getClass().getResource("/client/scenes/CardCell.fxml"));
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
            controller.setText(item.getTitle());
            controller.setOnButtonClick(event -> {
                // Handle button click
            });
            setGraphic(loader.getRoot());
        }
    }
}