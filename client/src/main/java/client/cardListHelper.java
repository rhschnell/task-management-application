package client;
import commons.Card;
import commons.CardList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
public class cardListHelper {
    static private DataFormat cardFormat = new DataFormat("Card");

    /**
     * Returns a VBOX consisting of A close button,the name of a list and a list of Cards
     * @param systemCardList
     * @return
     */
    public static VBox createCard(CardList systemCardList)
    {
        ListView<Card> cardListView = new ListView<Card>();
        cardListView.setCellFactory(param -> new CardCell());
        cardListView.getItems().addAll(systemCardList.getCards());

        //Make the content draggable
        //TODO FILIP - NEED TO CREATE A SEPARATE COMMIT FOR THIS ISSUE
       // makeDraggable(cardListView);

        //The Vbox for the List
        VBox cardListBox = new VBox();
        // Set Vbox alignment to center
        cardListBox.setAlignment(Pos.CENTER);
        //The button for List deletion
        Button deleteButton = new Button();
        deleteButton.setText("x");
        deleteButton.setStyle("-fx-background-color: #64B594; -fx-text-fill: white; -fx-background-radius:10;");
        //deleteButton.setOnAction(event -> {// Call the function here});
        //The Label that is in the top of the page
        Label listTitle = new Label();
        listTitle.setText(systemCardList.getListTitle());
        Separator separator = new Separator();separator.setPrefHeight(10);separator.setVisible(false);
        //Add to the Vbox all the components
        cardListBox.getChildren().addAll(deleteButton,listTitle,cardListView,separator);
        //CardListBox.getChildren().add(testButton);
        cardListBox.setMaxWidth(220);
        return cardListBox;
    }

    /**
     * Make the items of a ListView<Card> draggable between them
     * @param cardListView
     */
    private static void makeDraggable(ListView<Card> cardListView)
    {
        //TODO FILIP - NEED TO CREATE A SEPARATE COMMIT FOR THIS ISSUE
        cardListView.setOnDragDetected(event -> {
            // Only start a drag-and-drop gesture if there's at least one item selected
            if (cardListView.getSelectionModel().getSelectedItem() != null) {
                Dragboard dragboard = cardListView.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                // Add the selected items to the dragboard
                content.put(cardFormat,cardListView.getSelectionModel().getSelectedItems().get(0));
                dragboard.setContent(content);
                event.consume();
            }
        });
        cardListView.setOnDragOver(event -> {
            // Only allow the MOVE transfer mode TODO I HAVE EDITED
            if (event.getGestureSource() != cardListView ) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        cardListView.setOnDragDropped(event -> {
            // Get the drop target and the dropped item(s)
            ListView<Card> dropTarget = (ListView<Card>) event.getGestureTarget();
            Card newCard = (Card) event.getDragboard().getContent(cardFormat);
            // Add the dropped items to the drop target
            dropTarget.getItems().add(newCard);
            // Remove the dropped items from the source list
            ListView<Card> dragSource = (ListView<Card>) event.getGestureSource();
            dragSource.getItems().remove(newCard);
            event.setDropCompleted(true);
            event.consume();
        });
    }
    public static Separator newSeparator()
    {
        Separator separator = new Separator();
        separator.setPrefWidth(20);
        separator.setVisible(false);
        return separator;
    }

}
