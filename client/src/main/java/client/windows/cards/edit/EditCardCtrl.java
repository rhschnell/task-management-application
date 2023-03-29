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
    }

    /**
     *
     * @param card
     */
    public void setCard(Card card)
    {
        service.setCard(card);
        removeAppliedTag(new Tag());
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
        ctrl.setAvailableTags(service.getAvailableTags());
        ctrl.setAppliedTags(service.getCard().getTags());
        ctrl.setEditCardCtrl(this);
        ctrl.setType("edit");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        String title = "Add tag";
        helperMethods.popUp(scene, title);
    }
    /**
     * Adds the tag to the card, adds the added tag to the VBOX.
     * @param tag The tag that is added and needs to be displayed in the appliedTagsVbox on the AddCard
     */
    public void applyTag(Tag tag)
    {
        service.applyTag(tag);
        var loader =  new MyFXML(createInjector(new MainModules()))
                .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
        CustomTagCellCtrl ctrl = loader.getKey();
        ctrl.setTagObject(tag,"viewTag");
        appliedTagsVbox.getChildren().add(loader.getValue());
    }

    /**
     * Removes the tag from the list of applied tags that will be later sent to the server, and refreshes
     * the AppliedTagsVbox by clearing it and adding again all the applied tags.
     * @param tag the tag that needs to be removed from the list of the applied tags
     */
    public void removeAppliedTag(Tag tag)
    {
        service.removeAppliedTag(tag);
        appliedTagsVbox.getChildren().clear();

        for(int i=0;i<service.getAppliedTags().size();i++)
        {
            var loader =  new MyFXML(createInjector(new MainModules()))
                    .load(CustomTagCellCtrl.class, "client", "windows", "tags","CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(service.getAppliedTags().get(i),"viewTag");
            appliedTagsVbox.getChildren().add(loader.getValue());
        }
    }

}
