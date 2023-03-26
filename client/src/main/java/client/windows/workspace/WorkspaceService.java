package client.windows.workspace;

import client.MyFXML;
import client.modules.ListModules;
import client.serverUtils.BoardUtils;
import client.utils.HelperMethods;
import client.utils.Scenes;
import client.windows.lists.list.ListCtrl;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static com.google.inject.Guice.createInjector;

public class WorkspaceService {
    private final HelperMethods hm;
    private final BoardUtils server;

    private Board shownBoard;

    @FXML
    private Label boardName;
    @FXML
    private Button boardNameButton;
    @FXML
    private HBox listContainer;
    @FXML
    private HBox boardControls;
    @FXML
    private TextField keyField;
    @FXML
    private Button addListButton;

    @Inject
    public WorkspaceService(HelperMethods hm, BoardUtils server) {
        this.hm = hm;
        this.server = server;
    }

    public void disconnect() {
        hm.setScene(Scenes.USER);
    }

    public void showBoard(String targetKey) {
        try {
            shownBoard = server.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null);
            server.insertBoard(shownBoard);
        }

        listContainer.getChildren().clear();
        boardName.setText(shownBoard.getTitle());
        boardNameButton.setText(shownBoard.getTitle());
        for (int i = 0; i < shownBoard.getCardLists().size(); i++) {
            var loader = new MyFXML(createInjector(new ListModules()))
                    .load(ListCtrl.class, "client", "windows", "lists", "List.fxml");
            CardList cardList = shownBoard.getCardLists().get(i);
            VBox list = (VBox) loader.getValue();
            ListCtrl ctrl = loader.getKey();
            ctrl.setCardList(cardList);
            ctrl.addCards(cardList);
            ctrl.setListTitle(cardList.getListTitle());
            listContainer.getChildren().add(list);
        }
        for (Node child : boardControls.getChildren())
            if (!child.isVisible())
                child.setVisible(true);
        if (!boardName.isVisible())
            boardName.setVisible(true);
        if (!boardNameButton.isVisible())
            boardNameButton.setVisible(true);
        if (!listContainer.isVisible())
            listContainer.setVisible(true);
    }

    public void refreshWorkspace() {
        // TODO
        // Check with server if update
        // if update -> ask server for ids of update items
        // update those locally
        String key = shownBoard.getKey();
        Board serverBoard = server.getBoard(key);
        if (!shownBoard.equals(serverBoard)) {
            showBoard(key);
        }
    }

    public void clearWorkspace() {
        shownBoard = null;
        boardName.setText("");
        boardNameButton.setText("");
        listContainer.getChildren().clear();
        boardName.setVisible(false);
        boardNameButton.setVisible(false);
        listContainer.setVisible(false);
        for (Node child : boardControls.getChildren())
            child.setVisible(false);
        addListButton.setVisible(false);
    }

    public void deleteBoard() {
        server.deleteBoard(shownBoard.getKey());
        clearWorkspace();
    }

    public void addList() {
        shownBoard.addList(new CardList("Temporary", new ArrayList<Card>()));
        server.insertBoard(shownBoard);
        refreshWorkspace();
    }



    // SETTERS FOR FXML OBJECTS

    public void setBoardName(Label boardName) {
        this.boardName = boardName;
    }

    public void setBoardNameButton(Button boardNameButton) {
        this.boardNameButton = boardNameButton;
    }

    public void setListContainer(HBox listContainer) {
        this.listContainer = listContainer;
    }

    public void setBoardControls(HBox boardControls) {
        this.boardControls = boardControls;
    }

    public void setKeyField(TextField keyField) {
        this.keyField = keyField;
    }

    public void setAddListButton(Button addListButton) {
        this.addListButton = addListButton;
    }
}
