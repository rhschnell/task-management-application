package client.windows.lists.cells;


import client.serverUtils.CardListUtils;
import client.serverUtils.CardUtils;
import client.windows.lists.list.ListCtrl;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class QuickAddCardCtrl {

    private CardUtils cardUtils;
    private final CardListUtils server;
    private ListCtrl listCtrl;
    @FXML
    private TextField cardTitle;
    @FXML
    private Button addButton;

    @Inject
    public QuickAddCardCtrl(CardUtils cardUtils, CardListUtils server, ListCtrl listCtrl) {
        this.cardUtils = cardUtils;
        this.server = server;
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
        Card card = new Card(cardTitle.getText());
//        cardUtils.insertCard(card);
        listCtrl.getCardList().addCard(card);
//        server.insertCardList(listCtrl.getCardList());
    }


    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

}
