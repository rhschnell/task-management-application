package client;
import commons.Card;
import commons.CardList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.VBox;


public class CardListHelper {
    private DataFormat cardFormat;
    public CardListHelper(DataFormat format) {
        this.cardFormat = format;
    }

    /**
     * Returns a VBOX consisting of A close button,the name of a list and a list of Cards
     * @param systemCardList
     * @return
     */
    public VBox createCard(CardList systemCardList)
    {
        ListView<Card> cardListView = new ListView<Card>();
        cardListView.setCellFactory(param -> {
            ListCell<Card> cell = new CardCell();
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    // Double-clicked the cell, handle the event here
                    Card selectedCard = cell.getItem();
                    System.out.println("Selected card: " + selectedCard);
                }
            });
            return cell;
        });
        cardListView.getItems().addAll(systemCardList.getCards());


        //Make the content draggable
        //TODO FILIP - NEED TO CREATE A SEPARATE COMMIT FOR THIS ISSUE
        //makeDraggable(cardListView);

        //The Vbox for the List
        VBox cardListBox = new VBox();
        // Set Vbox alignment to center
        cardListBox.setAlignment(Pos.CENTER);
        //The button for List deletion
        Button deleteButton = new Button();
        deleteButton.setText("x");
        deleteButton.setStyle("-fx-background-color: #64B594; -fx-text-fill: white; -fx-background-radius:10;");
        deleteButton.setOnAction(event -> {
            // Call the function here
            System.out.println(systemCardList.getListTitle());
        });

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


    public static Separator newSeparator()
    {
        Separator separator = new Separator();
        separator.setPrefWidth(20);
        separator.setVisible(false);
        return separator;
    }

}
