package client.windows.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DialogPopupCtrl {
    @FXML
    private Label message;
    @FXML
    private Button closeButton;

    /**
     * Method called when close button pressed
     */
    @FXML
    public void close() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    /**
     * Updates the text of the label to the message
     * @param message Message to show to the user
     */
    public void setMessage(String message) {
        this.message.setText(message);
    }
}
