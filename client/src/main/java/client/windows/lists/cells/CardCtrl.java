package client.windows.lists.cells;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.WebsocketUtils;
import client.utils.HelperMethods;
import client.windows.cards.edit.EditCardCtrl;
import client.windows.cards.view.ViewCardCtrl;
import client.windows.lists.delete.DeleteCardCtrl;
import client.windows.lists.list.ListCtrl;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import commons.Task;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.springframework.messaging.simp.stomp.StompSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;

public class CardCtrl implements Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private Label cardTitle;
    @FXML
    private ImageView editButton;
    @FXML
    private ImageView deleteButton;
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
    private final HelperMethods helperMethods;
    private  ListCtrl listCtrl;
    private WebsocketUtils websocketUtils;


    /**
     * Creates a new instance of CardCtrl
     *
     * @param service The CardService for this CardCtrl
     * @param helperMethods hm
     */
    @Inject
    public CardCtrl(CardService service, HelperMethods helperMethods,WebsocketUtils websocketUtils) {
        this.service = service;
        this.helperMethods = helperMethods;
        this.websocketUtils=websocketUtils;
    }

    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
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
            tagCircle1.setFill(Paint.valueOf(tags.get(0).getTagColor()));
            tagCircle1.setVisible(true);

        }
        if (tags!=null && tags.size() > 1) {
            tagCircle2.setFill(Paint.valueOf(tags.get(1).getTagColor()));
            tagCircle2.setVisible(true);

        }
        if (tags!=null && tags.size() > 2) {
            tagCircle3.setFill(Paint.valueOf(tags.get(2).getTagColor()));
            tagCircle3.setVisible(true);

        }
    }

    /**
     * Displays the DeleteList FXML into a new window (Popup).
     */

    public void delete() {
        var loader = new MyFXML(createInjector(new MainModules()))
                    .load(DeleteCardCtrl.class, "client", "windows", "lists", "delete", "DeleteCard.fxml");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setDeleteCard(card);
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
            if (event.getCode() == KeyCode.ENTER) {
                loader.getKey().delete();
            }
        });
        String title = "Delete a card";
        helperMethods.popUp(scene, title);
    }

    public void edit() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(EditCardCtrl.class, "client", "windows", "cards", "EditCard.fxml");
        Parent root = loader.getValue();
        Scene scene = new Scene(root);
        loader.getKey().setBoardKey(service.getBoardKey());
        loader.getKey().setCard(card);
        scene.getRoot().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                loader.getKey().escape();
            }
        });
        loader.getKey().displayTasks();
        String title = "Edit card";
        helperMethods.popUp(scene, title);
    }


    /**
     * This function is called when clicking, and when double-clicking within 300ms, the viewCard is opened
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
        helperMethods.popUp(scene, title);

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
        registerForCardUpdates();

    }

    public ImageView getEditButton() {
        return editButton;
    }

    public ImageView getDeleteButton() {
        return deleteButton;
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
        deleteButton.setCursor(Cursor.HAND);
        editButton.setCursor(Cursor.HAND);
    }
    ///WEBSOCKETS
    /**
     * Registers for the messages for this card, so when c=something is changed on this card
     * it gets updates cause the path contains the cardId, which is unique
     */
    public void registerForCardUpdates() {
        StompSession.Subscription subscriber =websocketUtils.
                registerForMessages("/topic/cards/"+card.getId(), Card.class, newCard -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //Updates the cardList so that when we insert it again we have the latest version
                            listCtrl.updateCardList();
                            //Updates the card to the most recent version
                            card=newCard;
                            //Updates the cardTitle
                            cardTitle.setText(newCard.getTitle());
                            //Updates the description indicator
                            if(newCard.getDescription()!=null)
                                setDescriptionIconVisible(true);
                            if(newCard.getDescription()==null || newCard.getDescription().equals(""))
                                setDescriptionIconVisible(false);
                            //Updates the tag indicator
                            setDisplayTags(newCard.getTags());
                            //Updates the subtasks indicator
                            if(newCard.getSubTasks()==null || newCard.getSubTasks().size()==0)
                                subtaskIndicator.setVisible(false);
                            if(newCard.getSubTasks()!=null && newCard.getSubTasks().size()>0) {
                                long completedTasks =
                                        newCard.getSubTasks().stream().filter(Task::isCompleted).count();
                                subtaskIndicator.setVisible(true);
                                subtaskIndicator.setText(String.format("%d/%d", completedTasks,
                                        newCard.getSubTasks().size()));
                            }
                        }
                    });
                });
        //Adds the subscriber to the list of subscribers so when something changed we can unsubscribe
        listCtrl.addSubscriber(subscriber);
    }
}

