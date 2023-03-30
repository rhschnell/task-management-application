package client.utils;

import client.MyFXML;
import client.windows.workspace.helpWindow.HelpWindowCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class HelperMethods {
    private Stage primaryStage;
    private Scene[] scenes;
    private DataFormat cardFormat;

    public HelperMethods() {
    }

    public void setCardFormat(DataFormat cardFormat) {
        this.cardFormat = cardFormat;
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     */
    public static void popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        if(!title.equals("Help Window"))
        {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.SLASH && event.isShiftDown()) {
                    var loader = new MyFXML(createInjector(new client.modules.MainModules()))
                            .load(HelpWindowCtrl.class, "client", "windows",
                                    "workspace", "helpWindow", "helpWindow.fxml");
                    Parent root = loader.getValue();
                    Scene helpScene = new Scene(root);
                    String helpTitle = "Help Window";
                    HelperMethods.popUp(helpScene, helpTitle);
                }

            });
        }
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.setResizable(false);
        popUp.showAndWait();
    }

    public DataFormat getCardFormat() {
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
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SLASH && event.isShiftDown()) {
                var loader = new MyFXML(createInjector(new client.modules.MainModules()))
                        .load(HelpWindowCtrl.class, "client", "windows",
                                "workspace", "helpWindow", "helpWindow.fxml");
                Parent root = loader.getValue();
                Scene helpScene = new Scene(root);
                String helpTitle = "Help Window";
                HelperMethods.popUp(helpScene, helpTitle);
            }

        });
    }
}
