package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
    private Scene createBoard;
    private CreateBoardCtrl createBoardCt;

    public BoardCtrl getBoardCt() {
        return boardCt;
    }

    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> scene,Pair<BoardCtrl,Parent> board,Pair<CreateBoardCtrl,Parent> createBoard) {
        this.primaryStage = primaryStage;

        this.loginCtrl = scene.getKey();
        this.login = new Scene(scene.getValue());

        this.boardCt = board.getKey();
        this.board = new Scene(board.getValue());

        this.createBoardCt = createBoard.getKey();
        this.createBoard = new Scene(createBoard.getValue());

        primaryStage.setMinHeight(576);
        primaryStage.setMinWidth(1024);
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
    public void createBoard() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(createBoard);
    }

    public static Stage popUp(Scene scene, String title) {
        Stage popUp = new Stage();
        popUp.setScene(scene);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);
        popUp.setResizable(false);
        popUp.setResizable(false);
        popUp.showAndWait();

        return popUp;
    }

}
