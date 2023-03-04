package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    Stage primaryStage;

    Scene login;

    private LoginCtrl loginCtrl;

    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> scene) {
        this.primaryStage = primaryStage;

        this.loginCtrl = scene.getKey();
        this.login = new Scene(scene.getValue());

        showLogin();
        primaryStage.show();
    }

    public void showLogin() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(login);
    }
}
