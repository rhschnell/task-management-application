package client.windows.customize;

import client.MyFXML;
import client.modules.MainModules;
import client.utils.HelperMethods;
import client.windows.customize.cards.CustomCardPresetCellCtrl;
import client.windows.customize.cards.add.AddCardPresetCtrl;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
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

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class CustomizeCtrl {
    private final CustomizeService service;
    private WorkspaceCtrl workspaceCtrl;
    private HelperMethods helperMethods;
    private Board board;
    private List<CardColorPreset> boardPresetList;
    private List<CardColorPreset> newPresetList;
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

    private CardColorPreset newDefault;
    private CardColorPreset pastDefault;


    /**
     * Constructor for CustomizeCtrl
     *
     * @param workspaceCtrl Instance of WorkspaceCtrl
     * @param service       Corresponding service
     * @param helperMethods Instance of HelperMethods
     */
    @Inject
    public CustomizeCtrl(WorkspaceCtrl workspaceCtrl, CustomizeService service, HelperMethods helperMethods) {
        this.workspaceCtrl = workspaceCtrl;
        this.service = service;
        this.helperMethods = helperMethods;
    }

    /**
     * Gets the board
     *
     * @return The board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Sets the shownBoard
     *
     * @param shownBoard The shown board to be set
     */
    public void setBoard(Board shownBoard) {
        board = shownBoard;
        boardBackgroundColor.setValue(Color.web(board.getBoardBackgroundColour()));
        boardFontColor.setValue(Color.web(board.getBoardFontColour()));
    }

    /**
     * Sets the workspace controller that this customization popup stems from
     *
     * @param workspaceCtrl The corresponding WorkspaceCtrl
     */
    public void setWorkspaceCtrl(WorkspaceCtrl workspaceCtrl) {
        this.workspaceCtrl = workspaceCtrl;

        // Also initialize the default colors from this workspace
        listBackgroundColor.setValue(Color.web(workspaceCtrl.getShownBoard().getListBackgroundColor()));
        listFontColor.setValue(Color.web(workspaceCtrl.getShownBoard().getListFontColor()));

        // Initializes the preset list with the presets from the board
        this.boardPresetList = workspaceCtrl.getShownBoard().getPresetList();
        this.newPresetList = new ArrayList<>(boardPresetList);
    }

    /**
     * Sets the past default and the new default card presets so when saved the actions can be done
     *
     * @param newDefault  the new default
     * @param pastDefault the past default
     */
    public void setToChangeDefault(CardColorPreset newDefault, CardColorPreset pastDefault) {
        this.newDefault = newDefault;
        this.pastDefault = pastDefault;
    }

    /**
     * Changes the default card presets with the updated user choice
     */
    public void setDefaultToChange() {

        if (!getBoard().getPresetList().contains(newDefault)) {
            getBoard().addPreset(newDefault);
            service.insertBoard(getBoard());
            refreshBoard();
            newDefault.setId(getBoard().getPresetList().get(getBoard().getPresetList().size() - 1).getId());
        }
        for (CardList cardList : getBoard().getCardLists()) {
            for (Card card : cardList.getCards()) {
                if (card.getPresets().get(0).getBackgroundColor().equals(pastDefault.getBackgroundColor())
                        && card.getPresets().get(0).getFontColor().equals(pastDefault.getFontColor())) {
                    card.setPresets(new ArrayList<>());
                    card.setPreset(newDefault);
                }
            }
        }
    }

    /**
     * Method to reset the board colors to default
     * Resets the colors of the board to default
     */
    public void resetBoard() {
        board.setBoardFontColour("000000");
        board.setBoardBackgroundColour("F6F6F6");
    }

    /**
     * Method to reset the font and background colors of the lists
     */
    public void resetLists() {
        String defaultBackground = "FFFFFF";
        String defaultFont = "000000";

        listBackgroundColor.setValue(Color.web(defaultBackground));
        listFontColor.setValue(Color.web(defaultFont));
    }

    /**
     * This method closes the customize window
     */
    public void close() {
        workspaceCtrl.updateBoard();
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    /**
     * Method to save the current made changes to the board colors
     */
    public void save() {
        ((Stage) closeButton.getScene().getWindow()).close();
        if (newDefault != null && pastDefault != null)
            setDefaultToChange();
        board.setPresetList(newPresetList);
        List<CardColorPreset> colorPresetsToDelete = new ArrayList<>(boardPresetList);
        colorPresetsToDelete.removeAll(newPresetList);
        for (CardColorPreset preset : colorPresetsToDelete) {
            service.deletePreset(preset);
        }

        board.setBoardBackgroundColour(boardBackgroundColor.getValue().toString().substring(2, 8));
        board.setBoardFontColour(boardFontColor.getValue().toString().substring(2, 8));

        board.setListFontColor(listFontColor.getValue().toString().substring(2, 8));
        board.setListBackgroundColor(listBackgroundColor.getValue().toString().substring(2, 8));

        service.insertBoard(board);
        workspaceCtrl.refreshWorkspace(false);
    }

    /**
     * This method displays the presets in the cardPresets box
     */
    public void displayPresetList() {
        for (CardColorPreset preset : newPresetList) {
            var loader = new MyFXML(createInjector(new MainModules()))
                    .load(CustomCardPresetCellCtrl.class,
                            "client", "windows", "customize", "cards", "CustomCardPresetCell.fxml");
            CustomCardPresetCellCtrl ctrl = loader.getKey();
            ctrl.setBoard(board);
            ctrl.setPresetObject(preset);
            ctrl.setCustomizeCtrl(this);
            ctrl.setWorkspaceCtrl(workspaceCtrl);
            ctrl.setPresetList(newPresetList);

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
     * Refreshes the board with the last version
     */
    public void refreshBoard() {
        board = service.getBoard(board.getKey());
    }

    /**
     * This method opens a popup window where the user can add new card color presets
     */
    public void addCardPreset() {
        var loader = new MyFXML(createInjector(new MainModules()))
                .load(AddCardPresetCtrl.class,
                        "client", "windows", "customize", "cards", "add", "AddCardPreset.fxml");

        loader.getKey().setCustomizeCtrl(this);
        loader.getKey().setWorkspaceCtrl(workspaceCtrl);

        Parent root = loader.getValue();
        Scene scene = new Scene(root);

        String title = "Create Preset";
        helperMethods.popUp(scene, title);
    }

    /**
     * Adds a preset to the presetList
     *
     * @param preset The preset to be added
     */
    public void addPreset(CardColorPreset preset) {
        this.newPresetList.add(preset);
    }

}

