/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Label boardName;

    @FXML
    private Button secondBoardNameButton;

    @FXML
    private HBox myField;
    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * TODO Calls the function addCard when the button is pressed and adds a new Card to the First List
     */
    @FXML
    public void addCard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/AddCard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String title = "Create Card";
        mainCtrl.popUp(scene, title);
    }

    /**
     * Return's to the main screen
     */
    @FXML
    public void escapeBoard() {
        mainCtrl.showLogin();
    }

    /**
     * Initialize the board
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        Board systemBoard = testWithoutDb("First Board");
        boardName.setText(systemBoard.getTitle());
        secondBoardNameButton.setText(systemBoard.getTitle());
        createBoard(systemBoard);
    }

    /**
     * TODO Get the information from the database but for testing reasons created
     * @param myBoard
     */
    public void createBoard(Board myBoard)
    {
        for(int i = 0; i < myBoard.getCardLists().size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/List.fxml"));
            try {
                VBox list = loader.load();
                ListCtrl ctrl = loader.getController();
                ctrl.addCards(myBoard.getCardLists().get(i));
                myField.getChildren().add(list);

            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    public Board testWithoutDb(String boardName)
    {
        ArrayList<CardList> systemCardList= new ArrayList<>();
        systemCardList.add(testWithoutDatabase("TODO"));
        systemCardList.add(testWithoutDatabase("DONE"));
        systemCardList.add(testWithoutDatabase("TRASH"));
        Board myBoard = new Board(boardName, "no-key", systemCardList);
        return myBoard;
    }
    public CardList testWithoutDatabase(String cardListTitle)
    {
        Card p1 = new Card("Cleaning","To clean the floor","white",new ArrayList<>(),new ArrayList<>());
        Card p2 = new Card("Working","To work for the company","gray",new ArrayList<>(),new ArrayList<>());
        Card p3 = new Card("Washing","De wash the clothes","white",new ArrayList<>(),new ArrayList<>());
        Card p4 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p5 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        Card p6 = new Card("Dishes","Wash the dishes","blue",new ArrayList<>(),new ArrayList<>());
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(p1);
        cards.add(p2);
        cards.add(p3);
        cards.add(p4);
        cards.add(p5);
        cards.add(p6);
        cards.add(p6);
        cards.add(p6);
        cards.add(p6);
        CardList systemCard = new CardList(cardListTitle,cards);
        return systemCard;
    }

    public void refresh()
    {
        myField.getChildren().remove(0);
    }

}