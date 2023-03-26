package client.scenes.ListManagement;

import client.utils.CardListUtils;
import client.utils.CardUtils;
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
        cardUtils.addCard(card);
        listCtrl.getCardList().addCard(card);
        server.addCardList(listCtrl.getCardList());
        listCtrl.getMainCtrl().getWorkspaceCtrl().refreshWorkspace();
    }


    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }
}
