package client.windows.cards.editCard;

import client.MainCtrl;
import client.utils.CardUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.google.inject.Inject;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCardCtrl implements Initializable {

    private CardUtils server;
    private MainCtrl mainCtrl;
    private Card card;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;

    @FXML
    private Button saveButton;

    private Scene viewCardScene;

    public void setViewCardScene(Scene viewCardScene) {
        this.viewCardScene = viewCardScene;
    }
   
    @Inject
    public EditCardCtrl(CardUtils server) {
        this.server = server;
    }

    /**
     *
     * @param card
     */
    public void setCard(Card card)
    {
        this.card = card;
        setCardTitle(card.getTitle());
        setCardDescription(card.getDescription());
    }

    /**
     *
     * @param title
     */
    public void setCardTitle(String title)
    {
        cardTitle.setText(title);
    }

    /**
     *
     * @param description
     */
    public void setCardDescription(String description)
    {
        cardDescription.setText(description);
    }

    public void save()
    {
        String title=cardTitle.getText();
        String description=cardDescription.getText();
        card.setTitle(title);
        card.setDescription(description);
        server.insertCard(card);
        ((Stage)saveButton.getScene().getWindow()).close();
        ((Stage)viewCardScene.getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
