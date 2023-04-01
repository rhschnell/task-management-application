package client.windows.customize;

import client.serverUtils.BoardUtils;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomizeCtrl {
    @FXML
    private VBox layout;
    private WorkspaceCtrl workspaceCtrl;
    private Board board;
    private BoardUtils utils;

    @FXML
    private Button resetBoardColorButton;
    @FXML
    private Button resetListColorButton;
    @FXML
    private Button closeButton;

    @FXML
    private ColorPicker boardBackgroundColor;
    @FXML
    private ColorPicker boardFontColor;
    @FXML
    private ColorPicker listBackgroundColor;
    @FXML
    private ColorPicker listFontColor;

    @FXML
    private VBox cardColors;

    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, BoardUtils utils){
        this.workspaceCtrl = workspaceCtrl;
        this.utils = utils;
    }

    @FXML
    public void setBoardBackgroundColor(){
        System.out.println("-fx-background-color: #"+boardBackgroundColor.getValue().toString().substring(2,8));
        board.setBackgroundColour(boardBackgroundColor.getValue().toString().substring(2,8));
    }

    @FXML
    public void setBoardFontColor()
    {
    }

    public void close(){
        ((Stage)closeButton.getScene().getWindow()).close();
        utils.insertBoard(board);
    }

    public void setBoard(Board shownBoard) {
        board = shownBoard;
    }
}

