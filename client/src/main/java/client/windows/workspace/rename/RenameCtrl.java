package client.windows.workspace.rename;

import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RenameCtrl {
    private final RenameService service;

    private WorkspaceCtrl workspaceCtrl;

    @FXML
    private TextField inputField;

    @Inject
    public RenameCtrl(RenameService service) {
        this.service = service;
    }

    public void save() {
        Board board = workspaceCtrl.getShownBoard();
        board.setTitle(inputField.getText());
        System.out.println(inputField.getText());
        service.insertBoard(board);
        close();
    }

    public void cancel() {
        inputField.clear();
        close();
    }

    private void close() {
        ((Stage)inputField.getScene().getWindow()).close();
    }

    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
        this.inputField.setText(workspaceCtrl.getShownBoard().getTitle());
    }
}
