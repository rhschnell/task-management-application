package client.utils;/*package client.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MyListCell extends ListCell<String> {

    private final HBox hbox;
    private final Label label;
    private final Button button;

    public MyListCell() {
        super();
        hbox = new HBox();
        label = new Label();
        button = new Button("Button");
        hbox.getChildren().addAll(label, button);
        hbox.setSpacing(10);
        HBox.setHgrow(label, Priority.ALWAYS);
        button.setOnAction(event -> {
            // handle button action
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            label.setText(item);
            button.setText(item);
            setGraphic(hbox);
        }
    }
}
 */

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