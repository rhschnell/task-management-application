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
package client.windows.adminview.deleteBoard;

import client.windows.adminview.boardSpace.AdminCtrl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class DeleteBoardCtrl {
    private final DeleteBoardService service;
    private String boardKey;
    private AdminCtrl adminCtrl;

    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;

    /**
     * Constructor for DeleteBoardCtrl
     * @param service corresponding service
     */
    @Inject
    public DeleteBoardCtrl(DeleteBoardService service) {
        this.service = service;
    }

    /**
     * Setter for admin controller
     * @param adminCtrl admin controller
     */
    public void setAdminCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
    }

    /**
     * Setter for board key
     * @param key the key of the board to delete
     */
    public void setBoardKey(String key) {
        this.boardKey = key;
    }

    /**
     * This method cancels deleting the board
     */
    public void cancel(){
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method deletes the board from the server
     */
    public void delete(){
        ((Stage)deleteButton.getScene().getWindow()).close();
        service.deleteBoard(boardKey);
        adminCtrl.refreshWorkspace(true);
        adminCtrl.clearWorkspace();
    }
}
