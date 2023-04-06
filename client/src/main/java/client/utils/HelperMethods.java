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

import java.util.List;
import java.util.Map;

import static com.google.inject.Guice.createInjector;

public class HelperMethods {
    private Stage primaryStage;
    private Scene[] scenes;
    private DataFormat cardFormat;
    private Map<String, List<String>> memMap;
    private String serverIP;

    /**
     * Creates a new HelperMethods instance
     */
    public HelperMethods() {
    }

    /**
     * Sets the data format of the card, used in dragging and dropping cards
     *
     * @param cardFormat The new card format
     */
    public void setCardFormat(DataFormat cardFormat) {
        this.cardFormat = cardFormat;
    }

    /**
     * Displays a new window(popup) consisting of a scene and with a custom title.
     *
     * @param scene represents the scene that needs to be shown in the popup.
     * @param title represents the popup's title.
     */
    public static void popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        if (!title.equals("Help Window")) {
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
        popUp.showAndWait();
    }

    /**
     * Gets the card format
     *
     * @return the card format
     */
    public DataFormat getCardFormat() {
        return cardFormat;
    }

    /**
     * Sets the scene
     *
     * @param s the scene to set
     */
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

    /**
     * Sets the primary stage
     *
     * @param scene the scene to set as a primary stage
     */
    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * Sets the scenes
     *
     * @param scenes the scenes to set
     */
    public void setScenes(Scene... scenes) {
        this.scenes = scenes;
    }

    /**
     * Sets the primary stage
     *
     * @param primaryStage the primary stage to set
     */
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

    /**
     * Sets the memory map
     *
     * @param serverToKeyListMap New map containing servers as keys and lists or board-keys as
     *                           values
     */
    public void setMemMap(Map<String, List<String>> serverToKeyListMap) {
        this.memMap = serverToKeyListMap;
    }

    /**
     * Returns the map containing combinations of server IP addresses and corresponding boards
     * hosted on that server that the client has connected with.
     *
     * @return The memory map
     */
    public Map<String, List<String>> getMemMap() {
        return memMap;
    }

    /**
     * Gets the IP address of the server
     *
     * @return The server's IP address
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Sets the IP address of the server
     *
     * @param serverIP The new server IP address
     */
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }
}
