package client.windows.cards.edit;

import client.MyFXML;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.tags.view.CustomTagCellCtrl;
import client.windows.tags.view.TagListCtrl;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.google.inject.Inject;
import client.modules.MainModules;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static com.google.inject.Guice.createInjector;

public class EditCardCtrl implements Initializable {
    private final EditCardService service;

    @FXML
    private TextField cardTitle;
    @FXML
    private TextArea cardDescription;
    @FXML
    private Button saveButton;
    private HelperMethods helperMethods;
    private ViewCardCtrl viewCardCtrl;

    @FXML
    private VBox appliedTagsVbox;

    private Card newCard;

    /**
     * Injects the service , the Helper Methods and the viewCardCtrl
     * @param service
     * @param helperMethods
     * @param viewCardCtrl
     */
    @Inject
    public EditCardCtrl(EditCardService service, HelperMethods helperMethods, ViewCardCtrl viewCardCtrl) {
        this.service = service;
        this.helperMethods=helperMethods;
        this.viewCardCtrl = viewCardCtrl;
        appliedTagsVbox = new VBox();
        newCard=new Card();
    }

    /**
     *
     * @param card
     */
    public void setCard(Card card)
    {
        service.setCard(card);
        newCard.setTags(card.getTags());
        setAppliedTags(card.getTags());
        setCardTitle(card.getTitle());
        setCardDescription(card.getDescription());
    }

    public String getBoardKey()
    {
        return service.getBoardKey();
    }
    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }

    /**
     *Sets he title of the card
     * @param title the title
     */
    public void setCardTitle(String title) {
        cardTitle.setText(title);
    }

    /**
     *Sets the description of the card
     * @param description the description
     */
    public void setCardDescription(String description) {
        cardDescription.setText(description);
    }


    /**
     * Saves the changes and closes the pop-up
     */
    public void save() {
        Card editedCard = service.getCard();
        String title=cardTitle.getText();
        String description=cardDescription.getText();
        editedCard.setTitle(title);
        editedCard.setTags(newCard.getTags());
        editedCard.setDescription(description);
        service.insertCard(editedCard);
        viewCardCtrl.applyTag();
        ((Stage)saveButton.getScene().getWindow()).close();

    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    /**
     * Displays the pop-up (TagList) in order to choose and add a tag.
     */
    public void editTagPopup() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(TagListCtrl.class, "client", "windows", "tags","TagList.fxml");
        TagListCtrl ctrl = loader.getKey();
        List<Tag> available = service.getTags();
        available.removeAll(newCard.getTags());
        ctrl.setAvailableTags(available);
        ctrl.setAppliedTags(newCard.getTags());
        ctrl.setEditCardCtrl(this);
        ctrl.setType("edit");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        helperMethods.popUp(scene, title);
    }


    public void setAppliedTags(List<Tag> appliedTags)
    {
        newCard.setTags(appliedTags);
        appliedTagsVbox.getChildren().clear();
        for(int i=0;i<appliedTags.size();i++) {
            service.applyTag(appliedTags.get(i));
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(appliedTags.get(i), "viewTag");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }

    }



}
