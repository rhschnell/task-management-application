package client.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CardCellController {
    @FXML
    private Label label;
    @FXML private Button button;

    /**
     * Sets the text of the CardCell ( Card name )
     * @param text
     */
    public void setText(String text) {
        label.setText(text);
    }

    public void setOnButtonClick(EventHandler<ActionEvent> handler) {
        button.setOnAction(handler);
    }
}

