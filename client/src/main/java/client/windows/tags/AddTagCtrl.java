package client.windows.tags;

import client.serverUtils.BoardUtils;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;

public class AddTagCtrl {
    private final BoardUtils server;
    private final WorkspaceCtrl workspaceCtrl;
    private TagOverviewCtrl tagOverviewCtrl;

    @FXML
    private TextField tagTitle;

    @FXML
    private ColorPicker tagColor;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addTagButton;

    @Inject
    public AddTagCtrl(BoardUtils server, WorkspaceCtrl workspaceCtrl, TagOverviewCtrl tagOverviewCtrl){
        this.server = server;
        this.workspaceCtrl = workspaceCtrl;
        this.tagOverviewCtrl = tagOverviewCtrl;
        tagColor=new ColorPicker();
    }

    public void cancel() {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    public void set(TagOverviewCtrl tagOverviewCtrl){
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    public void save() {
        ((Stage)addTagButton.getScene().getWindow()).close();
        Tag tag = new Tag(tagTitle.getText(), tagColor.getValue().toString());

        workspaceCtrl.getShownBoard().addTag(tag);
        server.insertBoard(workspaceCtrl.getShownBoard());

        tagOverviewCtrl.updateDisplayedTags();
    }
}
