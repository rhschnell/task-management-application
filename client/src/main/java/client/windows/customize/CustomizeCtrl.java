package client.windows.customize;

import client.MyFXML;
import client.modules.MainModules;
import client.serverUtils.BoardUtils;
import client.serverUtils.CardListUtils;
import client.utils.HelperMethods;
import client.windows.customize.cards.CustomCardPresetCellCtrl;
import client.windows.customize.cards.add.AddCardPresetCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.CardColorPreset;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

import static com.google.inject.Guice.createInjector;

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

    /**
     * Constructor for CustomizeCtrl
     * @param workspaceCtrl Instance of WorkspaceCtrl
     * @param boardUtils Instance of BoardUtils
     * @param cardListUtils Instance of CardListUtils
     */
    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, BoardUtils boardUtils, CardListUtils cardListUtils){
        this.workspaceCtrl = workspaceCtrl;
        this.boardUtils = boardUtils;
        this.cardListUtils = cardListUtils;
    }

    /**
     * Sets the workspace controller that this customization popup stems from
     * @param workspaceCtrl The corresponding WorkspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;

        // Also initialize the default colors from this workspace
        listBackgroundColor.setValue(Color.web(workspaceCtrl.getInitialListColor()));
        listFontColor.setValue(Color.web(workspaceCtrl.getInitialListFontColor()));
    }

    /**
     * Method to se the board's background color
     */
    @FXML
    public void setBoardBackgroundColor(){
        board.setBackgroundColour(boardBackgroundColor.getValue().toString().substring(2,8));
    }


    /**
     * Method to se the board's font color
     */
    @FXML
    public void setBoardFontColor() {
        board.setFontColour(boardFontColor.getValue().toString().substring(2,8));
    }

    /**
     * Method to set the list background color for all the lists
     */
    @FXML
    public void setListBackgroundColor(){
        String newColor = listBackgroundColor.getValue().toString().substring(2, 8);
        for(CardList list : lists){
            list.setBackgroundColor(newColor);
        }
        workspaceCtrl.setInitialListColor(newColor);
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
        String newColor = listFontColor.getValue().toString().substring(2, 8);
        for (CardList list : lists) {
            list.setFontColor(newColor);
        }

        // Also make sure that any text in lists that are going to be created after this is
        // going to get this color out of the box
        workspaceCtrl.setInitialListFontColor(newColor);
    }

    /**
     * Getter to get the list font color
     * @return The current font color of the list
     */
    @FXML
    public String getListFontColor() {
        return listFontColor.getValue().toString();
    }

    /**
     * Method to reset the board colors to default
     */
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
    public void resetLists() {
        String defaultBackground = "FFFFFF";
        String defaultFont = "000000";
        for (CardList list : lists) {
            list.setFontColor(defaultFont);
            list.setBackgroundColor(defaultBackground);
        }
        listBackgroundColor.setValue(Color.web(defaultBackground));
        listFontColor.setValue(Color.web(defaultFont));

        workspaceCtrl.setInitialListFontColor(defaultFont);
        workspaceCtrl.setInitialListColor(defaultBackground);
    }

    /**
     * This method closes the customize window
     */
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

    /**
     * This method displays the presets in the cardPresets box
     */
    public void displayPresetList() {
        List<CardColorPreset> presetList = getBoard().getPresetList();
        for(CardColorPreset preset : presetList){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellCtrl.class,
                            "client", "windows", "customize", "cards", "CustomCardPresetCell.fxml");
            CustomCardPresetCellCtrl ctrl = loader.getKey();
            ctrl.setPresetObject(preset);
            ctrl.setCustomizeCtrl(this);

            cardPresets.getChildren().add(loader.getValue());
        }
    }

    /**
     * This method updates the displayed presets
     */
    public void updateDisplayedPresets() {
        cardPresets.getChildren().clear();
        displayPresetList();
    }

    /**
     * Gets the board
     * @return The board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Sets the shownBoard
     * @param shownBoard The shown board to be set
     */
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
        if (!lists.isEmpty()) {
            // Set the color pickers to the corresponding colors of the (first) cardlist
            listBackgroundColor.setValue(Color.web(lists.get(0).getBackgroundColor()));
            listFontColor.setValue(Color.web(lists.get(0).getFontColor()));
        } else {
            // The lists might be empty, but the initial color might be set already.
            //TODO: implement this functionality
        }
    }

    /**
     * This method opens a popup window where the user can add new card color presets
     */
    public void addCardPreset() {
        var loader =  new MyFXML(createInjector(new MainModules()))
                .load(AddCardPresetCtrl.class,
                        "client", "windows", "customize", "cards", "add", "AddCardPreset.fxml");

        loader.getKey().setCustomizeCtrl(this);
        loader.getKey().setWorkspaceCtrl(workspaceCtrl);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create Preset";
        HelperMethods.popUp(scene, title);
    }
}

