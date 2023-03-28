package client.windows.tags;

import client.MyFXML;
import client.utils.HelperMethods;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable {
    private WorkspaceCtrl workspaceCtrl;

    private MyFXML myFXML;

    @FXML
    private VBox displayedTags;

    @FXML
    private Button addTagButton;

    @FXML
    private Button cancelButton;

    @Inject
    public TagOverviewCtrl(WorkspaceCtrl workspaceCtrl, MyFXML myFXML) {
        this.workspaceCtrl = workspaceCtrl;
        this.myFXML = myFXML;
        displayedTags = new VBox();
    }

    public void addTag() {
        var loader = myFXML.load(AddTagCtrl.class, "client", "windows", "tags", "AddTag.fxml");
        loader.getKey().set(this);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Add Tag";
        HelperMethods.popUp(scene, title);
    }

    public void displayTagList() {
        List<Tag> tagList = workspaceCtrl.getShownBoard().getTagList();
        for (Tag tag : tagList) {
            var loader = myFXML.load(CustomTagCellCtrl.class, "client", "windows", "tags", "CustomTagCell.fxml");
            CustomTagCellCtrl ctrl = loader.getKey();
            ctrl.setTagObject(tag, "viewTag");
            displayedTags.getChildren().add(loader.getValue());
        }
    }


    public void updateDisplayedTags(){
        displayedTags.getChildren().clear();
        displayTagList();
    }

    public void cancel(){
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayTagList();
    }
}
