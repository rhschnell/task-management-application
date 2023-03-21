package client;
import client.scenes.CardWindows.AddCardCtrl;
import client.scenes.ListManagement.CustomListCellCtrl;
import client.scenes.TagManagement.CustomTagCellCtrl;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.util.Pair;

import java.io.IOException;

public class CustomTagCell extends ListCell<Tag> {
    private CustomTagCellCtrl controller;

    private Pair<CustomTagCellCtrl,Parent> loader;

    /**
     * Creates the cell that will be displayed within list.
     */
    public CustomTagCell() {
           loader = Main.getFXML().load(CustomTagCellCtrl.class, "client", "scenes", "TagManagement", "CustomTagCell.fxml");
    }

    /**
     * Updates the list of Cards with a new object.
     * @param item The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    protected void updateItem(Tag item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            loader.getKey().setOnButtonClick(event -> {
                System.out.println(item.getColor());
            });
            setGraphic(loader.getValue());
        }
    }
}