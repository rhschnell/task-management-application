package client.scenes;

import client.MyFXML;
import client.scenes.ListManagement.QuickAddCardCtrl;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import static com.google.inject.Guice.createInjector;

public class QuickAddCardCell extends AnchorPane {

    private QuickAddCardCtrl controller;

    /**
     * Creates a new instance of QuickAddCardCell
     */
    public QuickAddCardCell() {

        Pair<QuickAddCardCtrl, Parent> loader =
                new MyFXML(createInjector()).load(QuickAddCardCtrl.class, "client", "scenes",
                        "ListManagement", "QuickAddCardCell.fxml");
        this.controller = loader.getKey();
    }
}
