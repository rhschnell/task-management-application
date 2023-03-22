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
package client.scenes.TagManagement;

import client.scenes.CardWindows.AddCardCtrl;;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;


public class TagListCtrl {

    private ServerUtils server;
    private AddCardCtrl addCardCtrl;
    @FXML
    private Button cancelButton;

    @FXML
    private VBox tagListBox;

    @Inject
    public TagListCtrl(ServerUtils server, AddCardCtrl addCardCtrl) {
        this.addCardCtrl = addCardCtrl;
        this.server = server;
        tagListBox=new VBox();
    }
    public void setAvailableTags(List<Tag> tagList)
    {
        for(int i=0; i<tagList.size(); i++) {
            var loader = addCardCtrl.getListCtrl().getMyFXML()
                    .load(CustomTagCellCtrl.class, "client", "scenes", "TagManagement", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(tagList.get(i));
            ctrl.setCtrl(this);
            tagListBox.getChildren().add(loader.getValue());
        }
    }
    public AddCardCtrl getCardCtrl() {
        return addCardCtrl;
    }

    public void escapeWindow() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

}