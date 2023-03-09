package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;

    private Scene login;

    private AddCardCtrl addCardCt;
    private Scene addCard;
    private BoardCtrl boardCt;
    private Scene board;
    private LoginCtrl loginCtrl;
    private Scene createBoard;
    private CreateBoardCtrl createBoardCt;

    public void initialize(Stage primaryStage, Pair<LoginCtrl, Parent> scene, Pair<AddCardCtrl,Parent> addCard,
                           Pair<BoardCtrl,Parent> board,Pair<CreateBoardCtrl,Parent> createBoard) {
        this.primaryStage = primaryStage;

        this.loginCtrl = scene.getKey();
        this.login = new Scene(scene.getValue());

        this.addCardCt = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

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


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public LoginCtrl getLoginCtrl() {
        return loginCtrl;
    }

    public void setLoginCtrl(LoginCtrl loginCtrl) {
        this.loginCtrl = loginCtrl;
    }
}
