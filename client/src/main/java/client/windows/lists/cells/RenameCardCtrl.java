package client.windows.lists.cells;

import client.windows.lists.list.ListCtrl;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RenameCardCtrl implements Initializable {

    @FXML
    private TextField inputField;

    @FXML
    private Button cancel;

    @FXML
    private Button save;

    private Card card;
    private RenameCardService service;
    private ListCtrl listCtrl;


    /**
     * Injectable constructor for the RenameCardCtrl
     * MUST call setCard in order to work properly
     * @see #setCard(Card)
     * @param service Injected parameter of corresponding service
     */
    @Inject
    public RenameCardCtrl(RenameCardService service){
        this.service = service;
    }

    /**
     * Sets the list controller associated to this card
     * @param listCtrl The list controller
     */
    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    /**
     * Sets the card to rename using this rename component
     * MUST be intitalized!
     * @param card The card to rename
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Saves the card into the database with the new title
     */
    public void save(){
        String newTitle = inputField.getText();
        if (newTitle == null || newTitle.isEmpty()) return; //TODO notify user
        this.card.setTitle(newTitle);
        service.insertCard(this.card);
        this.close();
        listCtrl.displayCards();
    }

    /**
     * Closes the window
     */
    public void cancel(){
        inputField.clear();
        close();
    }

    /**
     * This method is used to close the window.
     */
    private void close() {
        ((Stage)inputField.getScene().getWindow()).close();
    }

    /**
     * Sets the service for this controller
     * @param service The new RenameCardService
     */
    public void setService(RenameCardService service) {
        this.service = service;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputField.requestFocus();
        inputField.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case ENTER:
                    save();
                    break;
                case ESCAPE:
                    cancel();
                    break;
            }
        });
    }
}
