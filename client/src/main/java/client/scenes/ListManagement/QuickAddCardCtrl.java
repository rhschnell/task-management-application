package client.scenes.ListManagement;

import client.utils.CardUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class QuickAddCardCtrl {

    private CardUtils cardUtils;
    @FXML
    private TextField cardTitle;
    @FXML
    private Button addButton;

    @Inject
    public QuickAddCardCtrl(CardUtils cardUtils){
        this.cardUtils = cardUtils;
    }

    public void setAddButtonVisible(){
        this.addButton.setVisible(true);
        this.addButton.setDisable(false);
    }

    /**
     * Adds a new card with a title
     */
    public void addCard(){
        cardUtils.addCard(new Card(cardTitle.getText()));
    }


}
