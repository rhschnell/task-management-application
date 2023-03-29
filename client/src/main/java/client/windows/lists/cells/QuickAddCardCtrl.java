package client.windows.lists.cells;

import client.windows.lists.list.ListCtrl;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class QuickAddCardCtrl {

    private final CardService service;
    private ListCtrl listCtrl;
    @FXML
    private TextField cardTitle;
    @FXML
    private Button addButton;

    @Inject
    public QuickAddCardCtrl(CardService service, ListCtrl listCtrl) {
        this.service = service;
        this.listCtrl = listCtrl;
    }

    public void setAddButtonVisible() {
        this.addButton.setVisible(true);
        this.addButton.setDisable(false);
    }

    /**
     * Adds a new card with a title
     */
    public void addCard() {
        service.insertCard(new Card(cardTitle.getText()),listCtrl.getCardList());
    }

    /**
     * Set the ListCtrl the QuickAdd is on
     * @param listCtrl the listCtrl that needs to be setted
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

}
