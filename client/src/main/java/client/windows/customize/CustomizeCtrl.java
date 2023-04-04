package client.windows.customize;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class CustomizeCtrl {
    private WorkspaceCtrl workspaceCtrl;

    private Board board;
    private BoardUtils boardUtils;

    private List<CardList> lists;
    private CardListUtils cardListUtils;


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
    private VBox cardPresets;

    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, BoardUtils boardUtils, CardListUtils cardListUtils){
        this.workspaceCtrl = workspaceCtrl;
        this.boardUtils = boardUtils;
        this.cardListUtils = cardListUtils;
    }

    @FXML
    public void setBoardBackgroundColor(){
        board.setBackgroundColour(boardBackgroundColor.getValue().toString().substring(2,8));
    }

    @FXML
    public void setBoardFontColor() {
        board.setFontColour(boardFontColor.getValue().toString().substring(2,8));
    }

    /**
     * Method to set the list background color for all the lists
     */
    @FXML
    public void setListBackgroundColor(){
        for(CardList list : lists){
            list.setBackgroundColor(listBackgroundColor.getValue().toString().substring(2, 8));
        }
    }

    /**
     * Getter to get the list background color
     * @return The current background color of the lists
     */
    @FXML
    public String getListBackgroundColor() {
        return listBackgroundColor.getValue().toString();
    }

    /**
     * Method to set the list font color for all the lists
     */
    @FXML
    public void setListFontColor() {
        for(CardList list : lists){
            list.setFontColor(listFontColor.getValue().toString().substring(2, 8));
        }
    }

    /**
     * Getter to get the list font color
     * @return The current font color of the list
     */
    @FXML
    public String getListFontColor() {
        return listFontColor.getValue().toString();
    }

    @FXML
    public void resetBoard() {
        board.setFontColour("000000");
        board.setBackgroundColour("FFFFFF");
        boardUtils.insertBoard(board);
        boardBackgroundColor.setValue(Color.web(board.getBackgroundColour()));
        boardFontColor.setValue(Color.web(board.getFontColour()));
    }

    /**
     * Method to reset the font and background colors of the lists
     */
    public void resetLists(){
        for(CardList list : lists){
            list.setFontColor("000000");
            list.setBackgroundColor("FFFFFF");
        }
        listBackgroundColor.setValue(Color.web("FFFFFF"));
        listFontColor.setValue(Color.web("000000"));
    }

    public void close(){
        ((Stage)closeButton.getScene().getWindow()).close();
    }

    /**
     * Method to save the current made changes to the board colors
     */
    public void save() {
        for(CardList list : lists){
            cardListUtils.insertCardList(list);
        }
        boardUtils.insertBoard(board);
        workspaceCtrl.refreshWorkspace();
        ((Stage)closeButton.getScene().getWindow()).close();
    }

    public void displayPresetList() {
        List<CardColorPreset> presetList = getBoard().getPresetList();

        for(CardColorPreset preset : presetList){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellCtrl.class, "client", "windows", "customize", "cards", "CustomCardPresetCell.fxml");
            CustomCardPresetCellCtrl ctrl = loader.getKey();
            ctrl.setPresetObject(preset);
            ctrl.setCustomizeCtrl(this);

            cardPresets.getChildren().add(loader.getValue());
        }
    }

    public void updateDisplayedPresets() {
        cardPresets.getChildren().clear();
        displayPresetList();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board shownBoard) {
        board = shownBoard;
        boardBackgroundColor.setValue(Color.web(board.getBackgroundColour()));
        boardFontColor.setValue(Color.web(board.getFontColour()));
    }

    /**
     * Method to set the cardlists that are shown on the board
     * @param cardLists The cardlists
     */
    public void setLists(List<CardList> cardLists){
        lists = cardLists;
        if(!lists.isEmpty()){
            listBackgroundColor.setValue(Color.web(lists.get(0).getBackgroundColor()));
            listFontColor.setValue(Color.web(lists.get(0).getFontColor()));
        }
    }

    public void addCardPreset() {
        var loader =  new MyFXML(createInjector(new MainModules()))
                .load(AddCardPresetCtrl.class, "client", "windows", "customize", "cards", "add", "AddCardPreset.fxml");

        loader.getKey().setCustomizeCtrl(this);
        loader.getKey().setWorkspaceCtrl(workspaceCtrl);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create Preset";
        HelperMethods.popUp(scene, title);
    }
}

