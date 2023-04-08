package client.windows.customize;

import client.MyFXML;
import client.modules.MainModules;
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
import org.checkerframework.checker.units.qual.C;

import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CustomizeCtrl {
    private WorkspaceCtrl workspaceCtrl;
    private HelperMethods helperMethods;

    private final CustomizeService service;

    private Board board;
    private List<CardList> lists;
    private List<CardColorPreset> presetList;

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
     * @param service Corresponding service
     * @param helperMethods Instance of HelperMethods
     */
    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, CustomizeService service, HelperMethods helperMethods){
        this.workspaceCtrl = workspaceCtrl;
        this.service = service;
        this.helperMethods = helperMethods;
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

        // Initializes the preset list with the presets from the board
        this.presetList = workspaceCtrl.getShownBoard().getPresetList();
    }

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * Method to se the board's background color
=======
     * Setter for the board background color
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
     */
    @FXML
    public void setBoardBackgroundColor(){
        board.setBackgroundColour(boardBackgroundColor.getValue().toString().substring(2,8));
    }

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * Method to se the board's font color
=======
     * Setter for the board font color
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
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
        for(CardList list : lists) {
            list.setBackgroundColor(newColor);
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
        String newColor = listFontColor.getValue().toString().substring(2, 8);
        for (CardList list : lists) {
            list.setFontColor(newColor);
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

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * Method to reset the board colors to default
=======
     * Resets the colors of the board to default
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
     */
    @FXML
    public void resetBoard() {
        board.setFontColour("000000");
        board.setBackgroundColour("FFFFFF");
        service.insertBoard(board);
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
    }

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * This method closes the customize window
     */
    public void close(){
        ((Stage)closeButton.getScene().getWindow()).close();
=======
     * Closes the window/stage
     */
    public void close() {
        ((Stage) closeButton.getScene().getWindow()).close();
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
    }

    /**
     * Method to save the current made changes to the board colors
     */
    public void save() {
        ((Stage)closeButton.getScene().getWindow()).close();

        workspaceCtrl.setInitialListFontColor(listFontColor.getValue().toString().substring(2, 8));
        workspaceCtrl.setInitialListColor(listBackgroundColor.getValue().toString().substring(2, 8));

        board.setPresetList(presetList);
        service.insertBoard(board);
        workspaceCtrl.refreshWorkspace(true);
    }

    /**
     * This method displays the presets in the cardPresets box
     */
    public void displayPresetList() {
        for(CardColorPreset preset : presetList){
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellCtrl.class,
                            "client", "windows", "customize", "cards", "CustomCardPresetCell.fxml");
            CustomCardPresetCellCtrl ctrl = loader.getKey();
            ctrl.setBoard(workspaceCtrl.getShownBoard());
            ctrl.setPresetObject(preset);
            ctrl.setCustomizeCtrl(this);
            ctrl.setWorkspaceCtrl(workspaceCtrl);
            ctrl.setPresetList(presetList);

            cardPresets.getChildren().add(loader.getValue());
        }
    }

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * This method updates the displayed presets
     */
    public void updateDisplayedPresets() {
        cardPresets.getChildren().clear();
        displayPresetList();
    }

    /**
     * Gets the board
     * @return The board
=======
     * Getter for the board
     * @return the board
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
     */
    public Board getBoard() {
        return this.board;
    }

    /**
<<<<<<< client/src/main/java/client/windows/customize/CustomizeCtrl.java
     * Sets the shownBoard
     * @param shownBoard The shown board to be set
=======
     * Setter for the board
     * @param shownBoard the board to be set
>>>>>>> client/src/main/java/client/windows/customize/CustomizeCtrl.java
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
        helperMethods.popUp(scene, title);
    }

    public void addPreset(CardColorPreset preset) {
        this.presetList.add(preset);
    }
}

