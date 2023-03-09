package client;
import client.scenes.MyListCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class MyListCell extends ListCell<String> {
    private FXMLLoader loader;
    private MyListCellController controller;

    public MyListCell() {
        loader = new FXMLLoader(getClass().getResource("/client/scenes/MyListCell.fxml"));
        try {
            loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            controller.setText(item);
            controller.setOnButtonClick(event -> {
                // Handle button click
            });
            setGraphic(loader.getRoot());
        }
    }
}