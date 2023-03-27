package client.utils;

import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelperMethods {
    private Stage primaryStage;
    private Scene[] scenes;
    private static DataFormat cardFormat;

    public HelperMethods() {
        cardFormat = new DataFormat("card");
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     */
    public static void popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.setResizable(false);
        popUp.showAndWait();
    }

    public static DataFormat getCardFormat() {
        return cardFormat;
    }

    public void setScene(Scenes s) {
        int scene;
        switch (s) {
            case STARTUP:
                scene = 0;
                break;
            case USER:
                scene = 3;
                break;
            case ADMIN:
                scene = 1;
                break;
            case WORKSPACE:
                scene = 2;
                break;
            case ADMINVIEW:
                scene = 4;
                break;
            default:
                throw new IllegalArgumentException();
        }
        primaryStage.setScene(scenes[scene]);
    }

    public void setScenes(Scene... scenes) {
        this.scenes = scenes;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
