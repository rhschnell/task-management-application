package client;
import client.scenes.ListManagement.CustomListCellCtrl;
import client.scenes.TagManagement.CustomTagCellCtrl;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CustomTagCell extends ListCell<Tag> {
    private FXMLLoader loader;
    private CustomTagCellCtrl controller;

    /**
     * Creates the cell that will be displayed within list.
     */
    public CustomTagCell() {
        loader = new FXMLLoader(getClass().
                                getResource("/client/scenes/TagManagement/CustomTagCell.fxml"));
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
    protected void updateItem(Tag item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
           // controller.setTagTitle(item.getName());
            controller.setOnButtonClick(event -> {
                System.out.println(item.getColor());
                // Handle button click
                //controller.
                //here i can pass the control i come from if i known
            });
            setGraphic(loader.getRoot());
        }
    }
}