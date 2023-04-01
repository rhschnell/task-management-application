package client.windows.lists.cells;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.cards.view.ViewCardCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CardCtrl {
    @FXML
    private AnchorPane pane;
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
    @FXML
    private ImageView descriptionIcon;

    @FXML
    private Label subtaskIndicator;


    private Card card;
    private long lastClickTime;

    private final CardService service;


    /**
     * Creates a new instance of CardCtrl
     *
     * @param service The CardService for this CardCtrl
     */
    @Inject
    public CardCtrl(CardService service) {
        this.service = service;
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
        if (tags!=null && tags.size() > 0) {
            tagCircle1.setFill(Paint.valueOf(tags.get(0).getColor()));
            tagCircle1.setVisible(true);

        }
        if (tags!=null && tags.size() > 1) {
            tagCircle2.setFill(Paint.valueOf(tags.get(1).getColor()));
            tagCircle2.setVisible(true);

        }
        if (tags!=null && tags.size() > 2) {
            tagCircle3.setFill(Paint.valueOf(tags.get(2).getColor()));
            tagCircle3.setVisible(true);

        }
    }

    /**
     * Sets the event to happen when interacting with the delete button
     */
    public void cardDeleteButton() {
        service.deleteCard(card);

    }

    /**
     * This functions is called when clicking, and when double clicking within 300ms the viewCard is opened
     */
    public void click() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < 300) {
            viewCard(card);
        }
        lastClickTime = clickTime;
    }

    /**
     * Opens the ViewCard FXML , displaying the cell card
     * @param cell the card that needs to be displayed
     */
    public void viewCard(Card cell) {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(ViewCardCtrl.class, "client", "windows", "cards", "ViewCard.fxml");

        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
        });
        ViewCardCtrl controller = loader.getKey();
        controller.setCard(cell);
        controller.setBoardKey(getBoardKey());
        controller.displayTasks();
        String title = "View Card";
        HelperMethods.popUp(scene, title);

    }

    /**
     * Returns the card
     * @return the card that the controller stores
     */
    public Card getCard() {
        return card;
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

    /**
     * Sets the subtasks indicator in the UI to reflect the number of completed subtasks for this
     * card as ratio completed/total
     *
     * @param completed The amount of completed or checked subtasks
     * @param total     The total amount of subtasks on this card
     */
    public void setSubtasksCompleted(long completed, long total) {
        if (completed > total) {
            throw new IllegalArgumentException("Cannot have more completed " +
                                               "subtasks than the total amount of" +
                                               " subtasks");
        }
        if (completed < 0) {
            throw new IllegalArgumentException("Cannot have negative amounts of completed or " +
                                               "total subtasks");
        }
        subtaskIndicator.setText(String.format("%d/%d", completed, total));
    }

    public void updateItem(Card item) {
        this.card = item;
        setCardTitle(item.getTitle());
        if(item.getTags()!=null)
            this.setDisplayTags(item.getTags());
        setDescriptionIconVisible(item.hasDescription());

        if(card.getSubTasks() !=null) {
            long subtasks = card.getSubTasks().size();
            if (subtasks > 0) {
                long completedTasks =
                        card.getSubTasks().stream().filter(Task::isCompleted).count();
                setSubtasksCompleted(completedTasks, card.getSubTasks().size());
                subtaskIndicator.setVisible(true);
            }
        }
    }

    private String getBoardKey()
    {
        return service.getBoardKey();
    }
    public void setBoardKey(String boardKey)
    {
        service.setBoardKey(boardKey);
    }
}

