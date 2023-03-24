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
package client.windows.lists.addList;

import client.MainCtrl;
import client.utils.BoardUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;


public class AddListCtrl {

    private final BoardUtils server;
    private final MainCtrl mainCtrl;


    @FXML
    private TextField listTitle;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;


    private String boardId;
    /**
     * Constructor for AddCardCtrl
     * @param server a server util
     * @param mainCtrl a main controller
     */
    @Inject
    public AddListCtrl(BoardUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        ScrollPane scrollPane ;
    }

    /**
     * This method cancels adding the created card to the list
     */
    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method saves the created card to the list
     *
     */
    public void setBoardId(String boardId)
    {
        this.boardId = boardId;
    }
    public void save() {
        ((Stage)saveButton.getScene().getWindow()).close();
        CardList cardList = new CardList(
                listTitle.getText(),new ArrayList<>());

        mainCtrl.getWorkspaceCtrl().getShownBoard().addList(cardList);
        server.insertBoard(mainCtrl.getWorkspaceCtrl().getShownBoard());
        mainCtrl.getWorkspaceCtrl().refreshWorkspace();
    }
}