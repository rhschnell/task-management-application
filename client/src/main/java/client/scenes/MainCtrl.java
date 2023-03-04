package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    Stage primaryStage;

    Scene login;

    private AddCardCtrl addCardCt;
    private Scene addCard;
    private BoardCtrl boardCt;
    private Scene board;
    private LoginCtrl loginCtrl;

    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> scene, Pair<AddCardCtrl,Parent> addCard,Pair<BoardCtrl,Parent> board) {
        this.primaryStage = primaryStage;

        this.loginCtrl = scene.getKey();
        this.login = new Scene(scene.getValue());

        this.addCardCt = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.boardCt = board.getKey();
        this.board = new Scene(board.getValue());

        showLogin();
        primaryStage.show();
    }

    public void showLogin() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(login);
    }
    public void showBoard() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(board);
    }
    public void showCard() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(addCard);
    }

}
