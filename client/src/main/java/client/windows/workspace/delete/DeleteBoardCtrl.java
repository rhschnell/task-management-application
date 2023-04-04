package client.windows.workspace.delete;/*
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

import client.windows.workspace.boardSpace.WorkspaceCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class DeleteBoardCtrl{

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private WorkspaceCtrl workspaceCtrl;


    public void setWorkspaceCtrl (WorkspaceCtrl workspaceCtrl)
    {
        this.workspaceCtrl=workspaceCtrl;
    }

    /**
     * This method cancels deleting the list from the board
     */
    public void cancel(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    /**
     * This method deletes the list from the board
     */
    public void delete(){
        ((Stage) deleteButton.getScene().getWindow()).close();
        workspaceCtrl.deleteBoard();
    }
    public void escape(){
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
