package client.scenes.handlers;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class EnterHandler implements EventHandler<KeyEvent> {

    /**
     * Handles the event only if an Enter fired the event
     * @param event the event which occurred
     */
    @Override
    public void handle(KeyEvent event) {
        if (!event.getCode().equals(KeyCode.ENTER)) return;
        onEnter();

    }

    /**
     * The action that needs to be done on Enter
     */
    public abstract void onEnter();
}