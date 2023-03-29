package client.windows.workspace.rename;

import client.windows.adminview.boardSpace.AdminCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RenameCtrl {
    private final RenameService service;

    private WorkspaceCtrl workspaceCtrl;
    private AdminCtrl adminCtrl;
    private Boolean admin;

    @FXML
    private TextField inputField;

    /**
     * Injectable constructor for the RenameCtrl
     * After using this constructor setWorkspaceCtrl MUST be called.
     * @param service Injected parameter of corresponding service
     */
    @Inject
    public RenameCtrl(RenameService service) {
        this.service = service;
    }

    /**
     * This method is executed upon clicking the save button in the popup.
     * It sets the title of the board to the current text of the
     * inputField and then inserts this into the database through
     * the service.
     * Then it closes the window.
     */
    public void save() {
        Board board;
        if (admin) {
            board = adminCtrl.getShownBoard();
        } else {
            board = workspaceCtrl.getShownBoard();
        }
        board.setTitle(inputField.getText());
        service.insertBoard(board);
        close();
    }

    /**
     * This method is executed upon clicking the cancel button in the popup.
     * It clears the inputField and closes the window.
     */
    public void cancel() {
        inputField.clear();
        close();
    }

    /**
     * This method is used to close the window.
     */
    private void close() {
        ((Stage)inputField.getScene().getWindow()).close();
    }

    /**
     * Setter for workspaceCtrl, MUST be called after constructor.
     * @param workspaceCtrl the instance to be injected
     */
    public void setRemoteCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;
        this.inputField.setText(workspaceCtrl.getShownBoard().getTitle());
    }

    /**
     * Setter for adminCtrl, MUST be called after constructor.
     * @param adminCtrl the instance to be injected
     */
    public void setRemoteCtrl(AdminCtrl adminCtrl) {
        this.adminCtrl = adminCtrl;
        this.inputField.setText(adminCtrl.getShownBoard().getTitle());
    }


    /**
     * Setter for admin, MUST be called after constructor
     * @param admin true/false
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
