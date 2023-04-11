package client.windows.workspace.lock;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AccessDeniedCtrl {
    @FXML
    private Button cancelButton;

    /**
     * Method called when cancel button pressed
     */
    public void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
