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
package client.windows.lists;

import client.serverUtils.CardListUtils;
import client.windows.lists.list.ListCtrl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class DeleteListCtrl implements Initializable {
    private final CardListUtils server;
    private final ListCtrl listCtrl;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    /**
     * Constructor for DeleteListCtrl
     * @param server a server util
     * @param listCtrl a main controller
     */
    @Inject
    public DeleteListCtrl(CardListUtils server, ListCtrl listCtrl){
        this.listCtrl = listCtrl;
        this.server = server;
    }

    /**
     * This method cancels deleting the list from the board
     */
    public void cancel(){
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method deletes the list from the board
     */
    public void delete(){
        ((Stage)deleteButton.getScene().getWindow()).close();
        server.deleteCardList((int)listCtrl.getCardList().getId());
//        listCtrl.getMainCtrl().getWorkspaceCtrl().refreshWorkspace();
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
}
