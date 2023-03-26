package client.windows.lists.cells;

import client.MainCtrl;
import client.MyFXML;
import client.modules.ListModules;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CardCtrl {
    @FXML
    private Label cardTitle;
    @FXML
    private Button deleteButton;

    @FXML
    private Circle tagCircle1;

    @FXML
    private Circle tagCircle2;

    @FXML
    private Circle tagCircle3;

    private Card card;

    @FXML
    private ImageView descriptionIcon;

    @FXML
    private AnchorPane pane;

    private long lastClickTime;

    private MainCtrl mainCtrl;

    /**
     * Creates a new instance of ListCellCtrl
     */
    @Inject
    public CardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets the title of the card shown in the overview of the list
     *
     * @param text Text to set title to
     */
    public void setCardTitle(String text) {
        cardTitle.setText(text);
    }

    /**
     * Sets the first 3 tags to be displayed on the board overview page on tge card
     *
     * @param tags the list of tags that the card has
     */
    public void setDisplayTags(List<Tag> tags) {
        tagCircle1.setVisible(false);
        tagCircle2.setVisible(false);
        tagCircle3.setVisible(false);
        if (tags.size() > 0) {
            tagCircle1.setFill(Paint.valueOf(tags.get(0).getColor()));
            tagCircle1.setVisible(true);

        }
        if (tags.size() > 1) {
            tagCircle2.setFill(Paint.valueOf(tags.get(1).getColor()));
            tagCircle2.setVisible(true);

        }
        if (tags.size() > 2) {
            tagCircle3.setFill(Paint.valueOf(tags.get(2).getColor()));
            tagCircle3.setVisible(true);

        }
    }

    /**
     * Sets the event to happen when interacting with the delete button
     *
     * @param handler the event to happen
     */
    public void setOnButtonClick(EventHandler<ActionEvent> handler) {
        deleteButton.setOnAction(handler);
    }

    public void click() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < 300) { // detect double-click
            viewCard(card); // your method to open a file
        }
        lastClickTime = clickTime;
    }

    public void viewCard(Card cell) {
        var loader = new MyFXML(createInjector(new ListModules()))
                .load(ViewCardCtrl.class, "client", "scenes", "CardWindows", "ViewCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        ViewCardCtrl controller = loader.getKey();
        controller.setCard(cell);

        String title = "View Card";
        HelperMethods.popUp(scene, title);
    }


    /**
     * Sets the visibility of the icon that indicates that a card has a description
     *
     * @param visible Boolean indicating the appropriate visibility status of the icon
     */
    public void setDescriptionIconVisible(boolean visible) {
        descriptionIcon.setVisible(visible);
    }

    /**
     * Updates the list of Cards with a new object.
     *
     * @param item The new item for the cell.
     */

    public void updateItem(Card item) {
        this.card = item;
        setCardTitle(item.getTitle());
        this.setDisplayTags(item.getTags());
        setOnButtonClick(event -> {
            System.out.println(item.getTitle());
            // Handle button click
        });
        setDescriptionIconVisible(item.hasDescription());
    }
}

